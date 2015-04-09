/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.chovanecm.snow;

import cz.chovanecm.snow.api.SnowClient;
import cz.chovanecm.snow.records.SnowScript;
import cz.chovanecm.snow.tables.ScriptSnowTable;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.junit.Test;

/**
 *
 * @author Martin
 */
public class TestSingleGet {

    public TestSingleGet() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    @Test
    public void testSingleGet() throws IOException {
        SnowClient client = new SnowClient("https://demo019.service-now.com", "admin", "admin", null, null);
        SnowScript script = client.getRecordBySysId(new ScriptSnowTable("sys_script_include", "script", "name"), "3cdeec09a997b940234722fdcbb24210", SnowScript.class);
        System.out.println(script.getScript());
        Files.write(Paths.get("D:/test.js"), script.getScript().getBytes());
    }
}
