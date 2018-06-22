package cz.chovanecm.snow.datalayer.rest.dao;

import cz.chovanecm.snow.datalayer.GenericDao;
import cz.chovanecm.snow.records.DbObject;

public interface DbObjectDao extends GenericDao<DbObject> {
    DbObject findTableByTableName(String tableName);
}
