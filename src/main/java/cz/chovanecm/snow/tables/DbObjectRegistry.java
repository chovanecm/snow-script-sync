package cz.chovanecm.snow.tables;

import cz.chovanecm.snow.records.DbObject;

public interface DbObjectRegistry {

    DbObject getObjectByName(String name);

}
