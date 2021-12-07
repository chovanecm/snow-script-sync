package cz.chovanecm.snow.datalayer.rest.dao;

import com.github.jsonj.JsonObject;
import cz.chovanecm.snow.datalayer.rest.SnowRestInterface;
import cz.chovanecm.snow.json.JsonManipulator;
import cz.chovanecm.snow.records.AbstractSnowRecord;
import cz.chovanecm.snow.records.SnowRecord;

public class SnowRecordScriptRestDao extends GenericBaseRestDao<SnowRecord> {
    public SnowRecordScriptRestDao(SnowRestInterface restInterface, String tableName) {
        super(restInterface, tableName);
    }

    @Override
    protected JsonManipulator<SnowRecord> getJsonManipulator() {
        return new JsonManipulator<>() {
            @Override
            protected SnowRecord initializeEmptyRecord() {
                return new AbstractSnowRecord();
            }

            @Override
            protected SnowRecord setMyFields(JsonObject json, SnowRecord record) {
                SnowRecord myRecord = super.setMyFields(json, record);
                json.forEach((key, val) -> myRecord.setAttributeValue(key, json.getString(key)));
                return myRecord;
            }
        };
    }
}
