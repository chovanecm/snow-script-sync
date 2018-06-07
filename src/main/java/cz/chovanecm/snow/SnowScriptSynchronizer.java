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
import cz.chovanecm.snow.datalayer.rest.BusinessRuleRestDao;
import cz.chovanecm.snow.files.FileRecordAccessor;
import cz.chovanecm.snow.records.DbObject;
import cz.chovanecm.snow.records.SnowScript;
import cz.chovanecm.snow.tables.ClientScriptTable;
import cz.chovanecm.snow.tables.DbObjectRegistry;
import cz.chovanecm.snow.tables.DbObjectTable;
import cz.chovanecm.snow.tables.ScriptSnowTable;
import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

/**
 * Main class
 */
public class SnowScriptSynchronizer {


    public static void run(SnowConnectorConfiguration connectorConfiguration, String destination) {

        // This allow us to access the ServiceNow instance
        SnowClient client = new SnowClient(connectorConfiguration);

        // Where the scripts download to
        Path root = Paths.get(destination);
        // Get a registry of all tables to create its folder structure later.
        DbObjectRegistry registry = new DbObjectRegistry(client.readAll(new DbObjectTable(), 100, DbObject.class));

        // TODO: what exactly is this used for?
        FileRecordAccessor fileAccessor = new FileRecordAccessor(registry, root);
        // List of the tables we will download scripts from.
        List<ScriptSnowTable> tables = Arrays.asList(new ScriptSnowTable("sys_script_include", "script", "name"),
                new ScriptSnowTable("sysevent_in_email_action", "script", "name"),
                new ScriptSnowTable("sys_script_fix", "script", "name"),
                new ClientScriptTable());
        /*
        for (ScriptSnowTable table : tables) {
            Iterable<SnowScript> iterable = client.readAll(table, 100, SnowScript.class);
            for (SnowScript script : iterable) {
                try {
                    script.save(fileAccessor);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }*/
        Flowable.fromIterable(tables)
                .flatMap(tableItem ->
                        Flowable.just(tableItem)
                                .subscribeOn(Schedulers.io())
                                .flatMap(tableToProcess -> Flowable.fromIterable(
                                        client.readAll(tableToProcess, 100, SnowScript.class)))
                                .mergeWith(Flowable.fromIterable(new BusinessRuleRestDao(client).getAll()))
                )
                .flatMap(script ->
                        Flowable.just(script)
                                .subscribeOn(Schedulers.io())
                                .map(s -> {
                                    s.save(fileAccessor);
                                    return s;
                                })
                ).blockingSubscribe(script -> {
                    //System.out.println(LocalTime.now().toString() + " Saved " + script.getScriptName());
                }
        );
        System.out.println("FINISHED.");
    }

}
