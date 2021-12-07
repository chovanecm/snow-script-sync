package cz.chovanecm.snow.datalayer.rest;

import com.github.jsonj.JsonObject;
import cz.chovanecm.snow.datalayer.rest.request.QueryGetRequest;
import cz.chovanecm.snow.datalayer.rest.request.SingleRecordGetRequest;

import java.io.IOException;

public interface SnowRestInterface {

    Iterable<JsonObject> getRecords(QueryGetRequest request);

    default Iterable<JsonObject> getRecords(String tableName) {
        return getRecords(QueryGetRequest.builder().tableName(tableName).build());
    }

    default JsonObject getRecord(String tableName, String sysId) throws IOException {
        return getRecord(SingleRecordGetRequest.builder().tableName(tableName).sysId(sysId).build());
    }

    JsonObject getRecord(SingleRecordGetRequest request) throws IOException;

    void saveRecord(String table, String sysId, com.google.gson.JsonObject object);
}
