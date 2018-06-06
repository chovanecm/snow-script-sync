package cz.chovanecm.snow.datalayer.rest;

import com.github.jsonj.JsonObject;

public interface SnowRestInterface {
    JsonObject getRecord(String tableName, String sysId);

    Iterable<JsonObject> getAllRecords(String tableName);
}
