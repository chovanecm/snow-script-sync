package cz.chovanecm.snow;

import cz.chovanecm.snow.records.BusinessRuleSnowScript;
import cz.chovanecm.snow.records.DbObject;
import cz.chovanecm.snow.records.SnowScript;
import cz.chovanecm.snow.tables.BusinessRuleTable;
import cz.chovanecm.snow.tables.DbObjectRegistry;
import cz.chovanecm.snow.tables.DbObjectTable;
import cz.chovanecm.snow.tables.ScriptSnowTable;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Martin
 */
public class SnowSycriptSynchronizer {

    static ExecutorService pool = Executors.newFixedThreadPool(4);

    public static void main(String[] args) throws IOException {
        SnowClient client = new SnowClient("https://demo019.service-now.com/", "admin", "admin", null, null);

        Path path = Files.createDirectories(Paths.get("/tmp/snow/sys_script_include"));
        for (final SnowScript script : client.readAll(new ScriptSnowTable("sys_script_include", "script", "name"), 100, SnowScript.class)) {
            Path file = path.resolve(script.getScriptName().replaceAll("[^\\. 0-9\\(\\),_a-zA-Z]", "_") + "_" + script.getSysId() + ".js");
            writeScript(file, script);
        }

        //dbobjects
        DbObjectRegistry registry = new DbObjectRegistry(client.readAll(new DbObjectTable(), 100, DbObject.class));

        //business rules
        path = Files.createDirectories(Paths.get("/tmp/snow/sys_script"));

        for (final BusinessRuleSnowScript script : client.readAll(new BusinessRuleTable(), 100, BusinessRuleSnowScript.class)) {
            Path file = path.resolve(script.getBusinessRuleOnTable().isEmpty() ? "." : script.getBusinessRuleOnTable()).resolve(script.getScriptName().replaceAll("[^\\. 0-9\\(\\),_a-zA-Z]", "_") + "_" + script.getSysId() + ".js");
            Files.createDirectories(file.getParent());
            writeScript(file, script);
        }

        for (DbObject object : registry.getAllObjects()) {
             Files.createDirectories(path.resolve(object.getName()));     
        }
        for (DbObject object : registry.getAllObjects()) {
            Path folder = path.resolve(object.getName());
            for (DbObject child : object.getChilds()) {
                Files.createSymbolicLink(folder.resolve(child.getName()), path.resolve(child.getName()));
            }
        }

        System.out.println("DONE! - Finishing IO");
        pool.shutdown();
        System.out.println("FINISHED.");
    }

    public static void writeScript(Path file, final SnowScript snowScript) {
        pool.execute(() -> {
            try {
                if (!Files.exists(file)) {
                    Files.createFile(file);
                    Files.write(file, snowScript.getScript().getBytes());
                    Files.setLastModifiedTime(file, FileTime.fromMillis(snowScript.getUpdatedOn().getTime()));

                }
            } catch (IOException ex) {
                Logger.getLogger(SnowSycriptSynchronizer.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        });

    }

}
