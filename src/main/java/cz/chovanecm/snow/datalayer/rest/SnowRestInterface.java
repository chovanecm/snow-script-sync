package cz.chovanecm.snow.datalayer.rest;

import com.github.jsonj.JsonObject;
import cz.chovanecm.snow.datalayer.rest.request.QueryGetRequest;
import cz.chovanecm.snow.datalayer.rest.request.SingleRecordGetRequest;

public interface SnowRestInterface {

    Iterable<JsonObject> getRecords(QueryGetRequest request);

    default Iterable<JsonObject> getRecords(String tableName) {
        return getRecords(QueryGetRequest.builder().tableName(tableName).build());
    }

    default JsonObject getRecord(String tableName, String sysId) {
        return getRecord(SingleRecordGetRequest.builder().tableName(tableName).sysId(sysId).build());
    }

    JsonObject getRecord(SingleRecordGetRequest request);

    void saveRecord(String table, String sysId, JsonObject object);
}
