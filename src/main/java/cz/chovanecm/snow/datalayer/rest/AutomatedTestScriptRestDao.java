package cz.chovanecm.snow.datalayer.rest;

import com.github.jsonj.JsonObject;
import cz.chovanecm.snow.datalayer.AutomatedTestScriptDao;
import cz.chovanecm.snow.datalayer.rest.request.QueryGetRequest;
import cz.chovanecm.snow.datalayer.rest.request.SingleRecordGetRequest;
import cz.chovanecm.snow.records.SnowScript;
import cz.chovanecm.snow.tables.ScriptSnowTable;
import io.reactivex.Flowable;
import lombok.Getter;

public class AutomatedTestScriptRestDao implements AutomatedTestScriptDao {
    private final ScriptSnowTable table = new ScriptSnowTable("sys_variable_value", "value", "sys_id");
    @Getter
    SnowRestInterface restInterface;

    @Override
    public Iterable<SnowScript> getAll() {
        return Flowable.fromIterable(
                getRestInterface().getRecords(
                        QueryGetRequest.builder()
                                .tableName("sys_variable_value")
                                .condition("document=sys_atf_step^variable.sys_name=Test%20script")
                                .build()))
                .map(it -> {
                    SnowScript script = table.getJsonManipulator().readFromJson(it);

                    JsonObject testStepRecord = getRestInterface().getRecord(
                            SingleRecordGetRequest.builder()
                                    .showDisplayValues(true)
                                    .tableName("sys_atf_step")
                                    .sysId(it.getObject("document_key").getString("value"))
                                    .build());
                    script.setScriptName(testStepRecord.getObject("test").getString("display_value") + "/" + testStepRecord.getString("order"));
                    return script;
                })
                .blockingIterable();
    }
}
