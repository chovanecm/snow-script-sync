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
import cz.chovanecm.snow.datalayer.ActiveRecord;
import cz.chovanecm.snow.datalayer.ActiveRecordFactory;
import cz.chovanecm.snow.datalayer.GenericDao;
import cz.chovanecm.snow.datalayer.file.FileActiveRecordFactory;
import cz.chovanecm.snow.datalayer.rest.dao.*;
import cz.chovanecm.snow.records.SnowScript;
import cz.chovanecm.snow.records.TableAwareSnowScript;
import cz.chovanecm.snow.tables.DbObjectRegistry;
import cz.chovanecm.snow.tables.PrefetchDbObjectRegistry;
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
    private final SnowConnectorConfiguration connectorConfiguration;
    private final String destination;
    private SnowClient snowClient;

    public SnowScriptSynchronizer(SnowConnectorConfiguration connectorConfiguration, String destination) {
        this.connectorConfiguration = connectorConfiguration;
        this.destination = destination;
    }

    public SnowClient getSnowClient() {
        if (snowClient == null) {
            snowClient = new SnowClient(connectorConfiguration);
        }
        return snowClient;
    }

    public void downloadAll() {

        // Where the scripts download to
        Path root = Paths.get(destination);
        // Get a registry of all tables to create its folder structure later.
        DbObjectDao dbObjectDao = getDbObjectDao();
        DbObjectRegistry registry = new PrefetchDbObjectRegistry(dbObjectDao);

        ActiveRecordFactory fileFactory = new FileActiveRecordFactory(root, registry);

        List<GenericDao<? extends SnowScript>> daosToUse = Arrays.asList(
                getAutomatedTestScriptDao(),
                getBusinessRuleDao(),
                getSnowScriptDao("sys_script_include"),
                getSnowScriptDao("sysevent_in_email_action"),
                getSnowScriptDao("sys_script_fix"),
                getClientScriptDao()
        );


        Flowable.fromIterable(daosToUse)
                .parallel()
                .runOn(Schedulers.io())
                .flatMap(dao -> Flowable.fromIterable(dao.getAll()))
                .map(script -> script.getActiveRecord(fileFactory))
                .doOnNext(ActiveRecord::save)
                .sequential()
                .blockingSubscribe();

        System.out.println("FINISHED.");
    }

    public DbObjectDao getDbObjectDao() {
        return new DbObjectRestDao(getSnowClient());
    }

    public GenericDao<TableAwareSnowScript> getClientScriptDao() {
        return new TableAwareScriptRestDao(getSnowClient(), "sys_script_client", "table");
    }

    public GenericDao<SnowScript> getSnowScriptDao(String scriptTableName) {
        return new SnowScriptRestDao(getSnowClient(), scriptTableName);
    }

    public GenericDao<SnowScript> getAutomatedTestScriptDao() {
        return new AutomatedTestScriptRestDao(getSnowClient());
    }

    public GenericDao<TableAwareSnowScript> getBusinessRuleDao() {
        return new TableAwareScriptRestDao(getSnowClient(), "sys_script", "collection");
    }

}
