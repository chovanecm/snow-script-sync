package cz.chovanecm.snow;

import cz.chovanecm.snow.api.SnowClient;
import cz.chovanecm.snow.files.FileRecordAccessor;
import cz.chovanecm.snow.records.BusinessRuleSnowScript;
import cz.chovanecm.snow.records.DbObject;
import cz.chovanecm.snow.records.SnowScript;
import cz.chovanecm.snow.tables.BusinessRuleTable;
import cz.chovanecm.snow.tables.ClientScriptTable;
import cz.chovanecm.snow.tables.DbObjectRegistry;
import cz.chovanecm.snow.tables.DbObjectTable;
import cz.chovanecm.snow.tables.ScriptSnowTable;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
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
        DbObjectRegistry registry = new DbObjectRegistry(client.readAll(new DbObjectTable(), 100, DbObject.class));

        FileRecordAccessor fileAccessor = new FileRecordAccessor(registry, root);
        fileAccessor.setInstanceURL(instance);
        List<ScriptSnowTable> tables = Arrays.asList(new ScriptSnowTable("sys_script_include", "script", "name"), new BusinessRuleTable(), new ClientScriptTable());
     
        for (ScriptSnowTable table : tables) {
            //@path contains sys_script_include
            for (final SnowScript script : client.readAll(table, 100, SnowScript.class)) {
                pool.execute(() -> {
                    try {
                        script.save(fileAccessor);
                    } catch (IOException ex) {
                        Logger.getLogger(SnowScriptSynchronizer.class.getName()).log(Level.SEVERE, null, ex);
                    }
                });
            }
        }
        System.out.println("DONE! - Finishing IO");
        pool.shutdown();
        System.out.println("FINISHED.");
    }

}
