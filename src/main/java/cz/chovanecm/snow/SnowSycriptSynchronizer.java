package cz.chovanecm.snow;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
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
        SnowScriptTable[] tables = {new SnowScriptTable("sys_script", "script", "name"), new SnowScriptTable("sys_script_include", "script", "name")};
        List<SnowScriptTable> scriptTables = Arrays.asList(tables);
        SnowClient client = new SnowClient("https://demo018.service-now.com/", "admin", "admin", null, null);
        
        ExecutorService pool = Executors.newFixedThreadPool(4);
        for (SnowScriptTable table : scriptTables) {
            Path path = Files.createDirectories(Paths.get("d:/snow/" + table.getTableName()));
            int i = 0;
            for (final SnowScript script : client.readAll(table, 100)) {
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
                            Path file = path.resolve(snowScript.getScriptName().replaceAll("[\\W][^\\.][^-][^_]", "_") + "_" + snowScript.getSysId() + ".js").normalize();
                            if (!Files.exists(file)) {
                                Files.createFile(file);
                                Files.write(file, snowScript.getScript().getBytes());
                                Files.setLastModifiedTime(file, FileTime.fromMillis(snowScript.getUpdated().getTime()));
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
