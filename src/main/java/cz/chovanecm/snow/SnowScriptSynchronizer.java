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
        GenericDao<DbObject> dbObjectDao = getDbObjectDao();
        DbObjectRegistry registry = new DbObjectRegistry(dbObjectDao.getAll());

        // TODO: what exactly is this used for?
        FileRecordAccessor fileAccessor = new FileRecordAccessor(registry, root);

        List<GenericDao<? extends SnowRecord>> daosToUse = Arrays.asList(
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
                .doOnNext(script -> script.save(fileAccessor))
                .sequential()
                .blockingSubscribe();

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
