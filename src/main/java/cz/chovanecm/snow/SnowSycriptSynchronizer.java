package cz.chovanecm.snow;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
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
public class SnowSycriptSynchronizer {

    public static void main(String[] args) throws IOException {
        ScriptSnowTable[] tables = {new ScriptSnowTable("sys_script", "script", "name"), new ScriptSnowTable("sys_script_include", "script", "name")};
        List<ScriptSnowTable> scriptTables = Arrays.asList(tables);
        SnowClient client = new SnowClient("https://demo019.service-now.com/", "admin", "admin", null, null);
        
        ExecutorService pool = Executors.newFixedThreadPool(4);
        for (ScriptSnowTable table : scriptTables) {
            Path path = Files.createDirectories(Paths.get("/tmp/snow/" + table.getTableName()));
            int i = 0;
            for (final SnowScript script : client.readAll(table, 100, SnowScript.class)) {
              //  System.out.println("Script: " + ++i + " Name: " + script.getScriptName());
             //   System.out.println(script.getScript());
               // System.out.println("-----------------------------------------");
                pool.execute(new Runnable() {
                    private SnowScript snowScript;

                    {
                        this.snowScript = script;
                    }

                    @Override
                    public void run() {
                        try {
                            Path file = path.resolve(snowScript.getScriptName().replaceAll("[^\\. 0-9\\(\\),_a-zA-Z]", "_") + "_" + snowScript.getSysId() + ".js").normalize();
                            if (!Files.exists(file)) {
                                Files.createFile(file);
                                Files.write(file, snowScript.getScript().getBytes());
                                Files.setLastModifiedTime(file, FileTime.fromMillis(snowScript.getUpdatedOn().getTime()));
                            }
                        } catch (IOException ex) {
                            Logger.getLogger(SnowSycriptSynchronizer.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }

                });
            }
        }
        System.out.println("DONE! - Finishing IO");
        pool.shutdown();
        System.out.println("FINISHED.");
    }

}
