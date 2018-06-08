package cz.chovanecm.snow.datalayer.rest;

import cz.chovanecm.snow.records.DbObject;
import cz.chovanecm.snow.tables.DbObjectTable;
import cz.chovanecm.snow.tables.SnowTable;

public class DbObjectRestDao extends GenericRestDao<DbObject> {
    SnowTable<DbObject> table = new DbObjectTable();

    public DbObjectRestDao(SnowRestInterface restInterface) {
        super(restInterface);
    }

    @Override
    protected SnowTable<DbObject> getTable() {
        return table;
    }
}
