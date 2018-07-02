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
import cz.chovanecm.snow.datalayer.file.impl.activerecord.AbstractScriptFileActiveRecord;
import cz.chovanecm.snow.datalayer.rest.dao.*;
import cz.chovanecm.snow.records.SnowScript;
import cz.chovanecm.snow.records.TableAwareSnowScript;
import cz.chovanecm.snow.tables.DbObjectRegistry;
import cz.chovanecm.snow.tables.PrefetchDbObjectRegistry;
import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;
import lombok.Value;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

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

    /**
     * Expects file snow-files.txt to contain information about localFile:category:sys_id
     * e.g.
     * src/script_include/DHLGltAutomaticRuleProcessor.js:script_include:d7bbb77a4f621300bc4df3117310c7b3
     */
    public void donwloadByFile() {
        final String fileName = "snow-files.txt";
        try {
            List<String> lines = Files.readAllLines(Paths.get(fileName));
            Function<String, Function<String, List<String>>> split;
            split = pattern -> str -> Arrays.asList(str.split(pattern));
            Function<String, List<String>> splitByColons = split.apply(":");
            Function<List<String>, SnowFilesRecord> listToSnowFilesRecord = list -> new SnowFilesRecord(list.get(0), list.get(1), list.get(2));
            Function<String, SnowFilesRecord> lineToSnowFilesRecord = splitByColons.andThen(listToSnowFilesRecord);

            Flowable.fromIterable(lines)
                    .map(lineToSnowFilesRecord::apply)
                    .subscribeOn(Schedulers.io())
                    .map(snowRecord -> {
                        SnowScript record = snowRecord.getServiceNowDao().get(snowRecord.getSysId());
                        return record.getActiveRecord(snowScript -> new AbstractScriptFileActiveRecord() {
                            @Override
                            public Path getFilePath() {
                                return Paths.get(snowRecord.getFileName());
                            }

                            @Override
                            public SnowScript getRecord() {
                                return record;
                            }
                        });
                    })
                    .blockingSubscribe(ActiveRecord::save);
        } catch (IOException e) {
            //TODO
            e.printStackTrace();
        }
    }

    @Value
    private class SnowFilesRecord {
        private String fileName;
        private String category;
        private String sysId;

        public GenericDao<SnowScript> getServiceNowDao() {
            switch (getCategory()) {
                case "script_include":
                    return getSnowScriptDao("sys_script_include");
                case "automated-test":
                    return getAutomatedTestScriptDao();
                default:
                    throw new IllegalArgumentException("Category " + getCategory() + " not supported yet.");
            }
        }

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
