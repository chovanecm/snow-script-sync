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
import cz.chovanecm.snow.datalayer.GenericDao;
import cz.chovanecm.snow.datalayer.rest.*;
import cz.chovanecm.snow.files.FileRecordAccessor;
import cz.chovanecm.snow.records.DbObject;
import cz.chovanecm.snow.records.SnowRecord;
import cz.chovanecm.snow.tables.DbObjectRegistry;
import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Main class
 */
public class SnowScriptSynchronizer {
    private final SnowConnectorConfiguration connectorConfiguration;
    private final String destination;
    private SnowClient snowClient;

    public SnowScriptSynchronizer(SnowConnectorConfiguration connectorConfiguration, String destination) {
        this.connectorConfiguration = connectorConfiguration;
        this.destination = destination;
    }

    public SnowClient getSnowClient() {
        if (snowClient == null) {
            return new SnowClient(connectorConfiguration);
        }
        return snowClient;
    }

    public void downloadAll() {

        // Where the scripts download to
        Path root = Paths.get(destination);
        // Get a registry of all tables to create its folder structure later.
        GenericDao<DbObject> dbObjectDao = getDbObjectDao();
        DbObjectRegistry registry = new DbObjectRegistry(dbObjectDao.getAll());

        // TODO: what exactly is this used for?
        FileRecordAccessor fileAccessor = new FileRecordAccessor(registry, root);
        // List of the tables we will download scripts from.
        /*List<ScriptSnowTable> tables = Arrays.asList(new ScriptSnowTable("sys_script_include", "script", "name"),
                new ScriptSnowTable("sysevent_in_email_action", "script", "name"),
                new ScriptSnowTable("sys_script_fix", "script", "name"),
                new ClientScriptTable());

        Flowable.fromIterable(tables)
                .flatMap(tableItem ->
                        Flowable.just(tableItem)
                                .subscribeOn(Schedulers.io())
                                .flatMap(tableToProcess -> Flowable.fromIterable(
                                        client.readAll(tableToProcess, 100, SnowScript.class)))
                                .mergeWith(Flowable.fromIterable(new BusinessRuleRestDao(client).getAll()))
                                .mergeWith(Flowable.fromIterable(new AutomatedTestScriptRestDao(client).getAll()))
                                .mergeWith(Flowable.fromIterable(new GenericScriptRestDao(client, "sys_script_include").getAll()))
                                .mergeWith(Flowable.fromIterable(new GenericScriptRestDao(client, "sysevent_in_email_action").getAll()))
                                .mergeWith(Flowable.fromIterable(new GenericScriptRestDao(client, "sys_script_fix").getAll()))
                                .mergeWith(Flowable.fromIterable(new ClientScriptRestDao(client).getAll()))
                )*/
        Flowable.fromIterable(getBusinessRuleDao().getAll())
                .observeOn(Schedulers.io())
                .cast(SnowRecord.class)
                .mergeWith(Flowable.fromIterable(getAutomatedTestScriptDao().getAll()))
                .mergeWith(Flowable.fromIterable(getSnowScriptDao("sys_script_include").getAll()))
                .mergeWith(Flowable.fromIterable(getSnowScriptDao("sysevent_in_email_action").getAll()))
                .mergeWith(Flowable.fromIterable(getSnowScriptDao("sys_script_fix").getAll()))
                .mergeWith(Flowable.fromIterable(getClientScriptDao().getAll()))
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

    public GenericDao<DbObject> getDbObjectDao() {
        return new DbObjectRestDao(getSnowClient());
    }

    public ClientScriptRestDao getClientScriptDao() {
        return new ClientScriptRestDao(getSnowClient());
    }

    public GenericScriptRestDao getSnowScriptDao(String scriptTableName) {
        return new GenericScriptRestDao(getSnowClient(), scriptTableName);
    }

    public AutomatedTestScriptRestDao getAutomatedTestScriptDao() {
        return new AutomatedTestScriptRestDao(getSnowClient());
    }

    public BusinessRuleRestDao getBusinessRuleDao() {
        return new BusinessRuleRestDao(getSnowClient());
    }

}
