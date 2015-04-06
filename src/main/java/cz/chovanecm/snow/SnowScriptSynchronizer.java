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
public class SnowScriptSynchronizer {

    static ExecutorService pool = Executors.newFixedThreadPool(4);

    public static void run(String instance, String user, String password, String proxy, Integer proxyPort, String destination) throws IOException {

        SnowClient client = new SnowClient(instance, user, password, proxy, proxyPort);
        Path root = Paths.get(destination);

        Path path = Files.createDirectories(root.resolve("sys_script_include/_inactive_")).getParent();
        //@path contains sys_script_include
        for (final SnowScript script : client.readAll(new ScriptSnowTable("sys_script_include", "script", "name"), 100, SnowScript.class)) {
            Path file = path.resolve(script.getScriptName().replaceAll("[^\\. 0-9\\(\\),_a-zA-Z]", "_") + "_" + script.getSysId() + ".js");
            if (!script.isActive()) {
                file = file.getParent().resolve("_inactive_").resolve(file.getFileName());
            }
            writeScript(file, script);
        }

        //dbobjects
        DbObjectRegistry registry = new DbObjectRegistry(client.readAll(new DbObjectTable(), 100, DbObject.class));

        //business rules
        path = Files.createDirectories(root.resolve("sys_script"));

        for (final BusinessRuleSnowScript script : client.readAll(new BusinessRuleTable(), 100, BusinessRuleSnowScript.class)) {
            Path filePath = path.resolve(getPath(registry.getObjectByName(script.getBusinessRuleOnTable())));
            Path file = filePath.resolve(script.getScriptName().replaceAll("[^\\. 0-9\\(\\),_a-zA-Z]", "_") + "_" + script.getSysId() + ".js");
            Files.createDirectories(file.getParent());
            if (!script.isActive()) {
                file = file.getParent().resolve("_inactive_").resolve(file.getFileName());
                Files.createDirectories(file.getParent());
            }
            writeScript(file, script);
        }

        System.out.println("DONE! - Finishing IO");
        pool.shutdown();
        System.out.println("FINISHED.");
    }

    public static Path getPath(DbObject object) {
        if (object == null) {
            return Paths.get(".");
        }
        Path path = Paths.get(object.getName());
        while (object.getSuperClass() != null) {
            object = object.getSuperClass();
            path = Paths.get(object.getName()).resolve(path);
        }
        return path;
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
                Logger.getLogger(SnowScriptSynchronizer.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        });

    }

}
