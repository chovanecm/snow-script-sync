package cz.chovanecm.snow.datalayer.rest;

import cz.chovanecm.snow.json.DbObjectJsonManipulator;
import cz.chovanecm.snow.json.JsonManipulator;
import cz.chovanecm.snow.records.DbObject;

public class DbObjectRestDao extends GenericBaseRestDao<DbObject> {


    public DbObjectRestDao(SnowRestInterface restInterface) {
        super(restInterface, "sys_db_object");
    }

    @Override
    protected JsonManipulator<DbObject> getJsonManipulator() {
        return new DbObjectJsonManipulator();
    }
}
