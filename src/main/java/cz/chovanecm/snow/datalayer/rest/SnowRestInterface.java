package cz.chovanecm.snow.datalayer.rest;

import com.github.jsonj.JsonObject;

public interface SnowRestInterface {

    Iterable<JsonObject> getRecords(SnowRestGetRequest request);

    default Iterable<JsonObject> getRecords(String tableName) {
        return getRecords(SnowRestQueryGetRequest.builder().tableName(tableName).build());
    }

    default JsonObject getRecord(String tableName, String sysId) {
        return getRecord(SingleRecordSnowRestGetGetRequest.builder().tableName(tableName).sysId(sysId).build());
    }

    JsonObject getRecord(SnowRestGetRequest request);
}
