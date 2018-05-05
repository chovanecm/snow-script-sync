/*
 * Snow Script Synchronizer is a tool helping developers to write scripts for ServiceNow
 *     Copyright (C) 2015-2017  Martin Chovanec <chovamar@fit.cvut.cz>
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package cz.chovanecm.snow;

import cz.chovanecm.snow.api.SnowClient;
import cz.chovanecm.snow.files.FileRecordAccessor;
import cz.chovanecm.snow.records.DbObject;
import cz.chovanecm.snow.records.SnowScript;
import cz.chovanecm.snow.tables.*;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Main class
 */
public class SnowScriptSynchronizer {

    // We will download and process scripts in parallel
    static ExecutorService pool = Executors.newFixedThreadPool(4);

    public static void run(SnowConnectorConfiguration connectorConfiguration, String destination) throws IOException {

        // This allow us to access the ServiceNow instance
        SnowClient client = new SnowClient(connectorConfiguration);

        // Where the scripts download to
        Path root = Paths.get(destination);
        // TODO: what exactly is this used for?
        DbObjectRegistry registry = new DbObjectRegistry(client.readAll(new DbObjectTable(), 100, DbObject.class));

        // TODO: what exactly is this used for?
        FileRecordAccessor fileAccessor = new FileRecordAccessor(registry, root);
        // List of the tables we will download scripts from.
        List<ScriptSnowTable> tables = Arrays.asList(new ScriptSnowTable("sys_script_include", "script", "name"), new ScriptSnowTable("sysevent_in_email_action", "script", "name"), new BusinessRuleTable(), new ClientScriptTable());

        for (ScriptSnowTable table : tables) {
            // now for each table, iterate through all the records and save them using fileAcessor.
            for (final SnowScript script : client.readAll(table, 100, SnowScript.class)) {
                pool.execute(() -> {
                    try {
                        script.save(fileAccessor);
                    } catch (IOException ex) {
                        Logger.getLogger(SnowScriptSynchronizer.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
                    }
                });
            }
        }
        System.out.println("DONE! - Finishing IO");
        pool.shutdown();
        System.out.println("FINISHED.");
    }

}
