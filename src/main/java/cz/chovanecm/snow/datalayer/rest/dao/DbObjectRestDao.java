package cz.chovanecm.snow.datalayer.rest.dao;

import cz.chovanecm.snow.datalayer.rest.SnowRestInterface;
import cz.chovanecm.snow.datalayer.rest.request.QueryGetRequest;
import cz.chovanecm.snow.json.DbObjectJsonManipulator;
import cz.chovanecm.snow.json.JsonManipulator;
import cz.chovanecm.snow.records.DbObject;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.schedulers.Schedulers;

import java.util.NoSuchElementException;

public class DbObjectRestDao extends GenericBaseRestDao<DbObject> implements DbObjectDao {


    public DbObjectRestDao(SnowRestInterface restInterface) {
        super(restInterface, "sys_db_object");
    }

    @Override
    protected JsonManipulator<DbObject> getJsonManipulator() {
        return new DbObjectJsonManipulator();
    }

    @Override
    public DbObject findTableByTableName(String tableName) {
        try {
            return Flowable.fromIterable(getRestInterface().getRecords(QueryGetRequest.builder()
                    .tableName("sys_db_object")
                    .condition("name=" + tableName)
                    .build()))
                    .observeOn(Schedulers.io())
                    .map(result -> getJsonManipulator().readFromJson(result))
                    .blockingFirst();
        } catch (NoSuchElementException ex) {
            DbObject unknown = new DbObject();
            unknown.setName("unknown");
            unknown.setSysId("NA");
            unknown.setSuperClassId("");
            return unknown;
        }

    }
}
