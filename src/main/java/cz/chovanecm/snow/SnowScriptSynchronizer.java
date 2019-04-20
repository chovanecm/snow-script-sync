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
import cz.chovanecm.snow.datalayer.*;
import cz.chovanecm.snow.datalayer.file.FileActiveRecordFactory;
import cz.chovanecm.snow.datalayer.file.impl.activerecord.AbstractScriptFileActiveRecord;
import cz.chovanecm.snow.datalayer.file.impl.dao.FileSystemDao;
import cz.chovanecm.snow.datalayer.rest.RestActiveRecordFactory;
import cz.chovanecm.snow.datalayer.rest.RestSnowVariable;
import cz.chovanecm.snow.datalayer.rest.dao.*;
import cz.chovanecm.snow.datalayer.rest.impl.activerecord.ChangeAwareRestActiveRecord;
import cz.chovanecm.snow.records.SnowRecord;
import cz.chovanecm.snow.records.SnowScript;
import cz.chovanecm.snow.records.TableAwareSnowScript;
import cz.chovanecm.snow.tables.DbObjectRegistry;
import cz.chovanecm.snow.tables.PrefetchDbObjectRegistry;
import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;
import lombok.Getter;
import lombok.Value;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Main class
 */
public class SnowScriptSynchronizer {
    private final SnowConnectorConfiguration connectorConfiguration;
    final String mappingFile = "snow-files.txt";
    private SnowClient snowClient;
    @Getter
    private final Path destination;

    public SnowClient getSnowClient() {
        if (snowClient == null) {
            snowClient = new SnowClient(connectorConfiguration);
        }
        return snowClient;
    }

    Function<String, Function<String, List<String>>> split = pattern -> str -> Arrays.asList(str.split(pattern));
    Function<String, List<String>> splitByColons = split.apply(":");
    Function<List<String>, SnowFilesRecord> listToSnowFilesRecord = list -> new SnowFilesRecord(list.get(0), list.get(1), list.get(2));
    Function<String, SnowFilesRecord> lineToSnowFilesRecord = splitByColons.andThen(listToSnowFilesRecord);

    public SnowScriptSynchronizer(SnowConnectorConfiguration connectorConfiguration, String destination) {
        this.connectorConfiguration = connectorConfiguration;
        this.destination = Paths.get(destination);
    }

    /**
     * Expects file snow-files.txt to contain information about localFile:category:sys_id
     * e.g.
     * src/script_include/DHLGltAutomaticRuleProcessor.js:script_include:d7bbb77a4f621300bc4df3117310c7b3
     */
    public void downloadByFile() {
        System.out.println("Re-downloading files!");
        try {
            List<String> lines = Files.readAllLines(getDestination().resolve(mappingFile));
            Flowable.fromIterable(lines)
                    .map(lineToSnowFilesRecord::apply)
                    .subscribeOn(Schedulers.io())
                    .map(snowRecord -> {
                        SnowScript record = snowRecord.getServiceNowDao().get(snowRecord.getSysId());
                        return record.getActiveRecord(snowScript -> new AbstractScriptFileActiveRecord() {
                            @Override
                            public Path getFilePath() {
                                return getDestination().resolve(snowRecord.getFileName());
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

    /**
     * @param filename
     * @return true or false based on the success
     */
    private boolean uploadFile(String filename) {
        System.out.println("Uploading " + filename);
        Path file = Paths.get(filename);
        if (!file.isAbsolute()) {
            file = getDestination().resolve(file);
        }
        try {
            List<String> lines = Files.readAllLines(getDestination().resolve(mappingFile));
            Path finalFile = file;
            SnowFilesRecord snowRecord = lines.stream().map(lineToSnowFilesRecord)
                    .filter(record -> getDestination().resolve(record.getFileName()).equals(finalFile))
                    .findFirst().orElseThrow(() -> new IOException("Could not resolve file " + finalFile));

            GenericDao<SnowScript> dao = new FileSystemDao(id -> getDestination().resolve(snowRecord.getFileName()), id -> snowRecord.getCategory());

            SnowScript script = dao.get(snowRecord.getSysId());

            RestActiveRecordFactory activeRecordFactory = new RestActiveRecordFactory(getSnowClient());

            //TODO oh no, another ugly hack
            switch (script.getCategory()) {
                case "script_include":
                    script.setCategory("sys_script_include");
                    break;
                case "automated-test":
                    script.setCategory("sys_variable_value");
                    activeRecordFactory = new RestActiveRecordFactory(getSnowClient(), "value");
                    ChangeAwareSnowRecord record = new ChangeAwareSnowRecord(getVariable(script.getSysId()).getRelatedRecord());
                    script.getActiveRecord(activeRecordFactory).save();
                    record.setAttributeValue("description", "Run server-side script");
                    new ChangeAwareRestActiveRecord(getSnowClient(), record).save();
                    return true;
                case "calculated_field":
                    script.setCategory("sys_dictionary");
                    activeRecordFactory = new RestActiveRecordFactory(getSnowClient(), "calculation");
                    break;
            }
            script.getActiveRecord(activeRecordFactory).save();
            return true;
        } catch (IOException e) {
            //TODO
            e.printStackTrace();
            return false;
        }
    }

    public void uploadFiles(List<String> filenames) {
        System.out.println("Files to upload: " + filenames.size() + ": " + filenames.stream().collect(Collectors.joining(", ")));

        Flowable.fromIterable(filenames)
                .parallel()
                .runOn(Schedulers.io())
                .map(this::uploadFile)
                .sequential()
                .blockingSubscribe();

    }

    private SnowVariable getVariable(String sysId) {
        SnowRecord variableRecord = new SnowRecordScriptRestDao(getSnowClient(), "sys_variable_value").get(sysId);
        return new RestSnowVariable(getSnowClient(), variableRecord);
    }

    public GenericDao<? extends SnowScript> getCalculatedFieldDao() {
        TableAwareScriptRestDao dao = new TableAwareScriptRestDao(getSnowClient(), "sys_dictionary",
                "calculation", "element", "name");
        setQuery(dao);
        dao.setQuery(dao.getQuery() + "^virtual=true");
        dao.setCategoryNameSupplier(() -> "calculated_field");
        return dao;
    }

    public void downloadAll() {

        // Where the scripts download to
        Path root = getDestination();
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
                getUiActionDao(),
                getClientScriptDao(),
                getCalculatedFieldDao()
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
        TableAwareScriptRestDao dao = new TableAwareScriptRestDao(getSnowClient(), "sys_script_client", "table");
        setQuery(dao);
        return dao;
    }

    public GenericDao<TableAwareSnowScript> getUiActionDao() {
        TableAwareScriptRestDao dao = new TableAwareScriptRestDao(getSnowClient(), "sys_ui_action", "table");
        setQuery(dao);
        return dao;
    }

    public GenericDao<SnowScript> getSnowScriptDao(String scriptTableName) {
        SnowScriptRestDao dao = new SnowScriptRestDao(getSnowClient(), scriptTableName);
        setQuery(dao);
        return dao;
    }

    public GenericDao<SnowScript> getAutomatedTestScriptDao() {
        AutomatedTestScriptRestDao dao = new AutomatedTestScriptRestDao(getSnowClient());
        setQuery(dao);
        return dao;
    }

    public GenericDao<TableAwareSnowScript> getBusinessRuleDao() {
        TableAwareScriptRestDao dao = new TableAwareScriptRestDao(getSnowClient(), "sys_script", "collection");
        setQuery(dao);
        return dao;
    }

    @Value
    private class SnowFilesRecord {
        private String fileName;
        private String category;
        private String sysId;

        public GenericDao<? extends SnowScript> getServiceNowDao() {
            switch (getCategory()) {
                case "script_include":
                    return getSnowScriptDao("sys_script_include");
                case "automated-test":
                    return getAutomatedTestScriptDao();
                case "calculated_field":
                    return getCalculatedFieldDao();
                default:
                    return getSnowScriptDao(getCategory());
            }
        }

    }

    private void setQuery(Filterable in) {
        // Nothing here in.setQuery("sys_updated_bySTARTSWITHsnc_operator");
    }

}
