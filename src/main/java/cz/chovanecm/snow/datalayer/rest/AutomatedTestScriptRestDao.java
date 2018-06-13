package cz.chovanecm.snow.datalayer.rest;

import com.github.jsonj.JsonObject;
import cz.chovanecm.snow.datalayer.AutomatedTestScriptDao;
import cz.chovanecm.snow.datalayer.rest.request.QueryGetRequest;
import cz.chovanecm.snow.datalayer.rest.request.SingleRecordGetRequest;
import cz.chovanecm.snow.json.JsonManipulator;
import cz.chovanecm.snow.json.SnowScriptJsonManipulator;
import cz.chovanecm.snow.records.SnowScript;
import io.reactivex.Flowable;
import lombok.Getter;

public class AutomatedTestScriptRestDao implements AutomatedTestScriptDao {
    private final SnowScriptJsonManipulator jsonManipulator = new SnowScriptJsonManipulator("value", "sys_id");

    public AutomatedTestScriptRestDao(SnowRestInterface restInterface) {
        this.restInterface = restInterface;
    }

    @Getter
    SnowRestInterface restInterface;

    @Override
    public SnowScript get(String id) {
        throw new RuntimeException("Get single Automated script is not supported (yet)");
    }

    @Override
    public Iterable<SnowScript> getAll() {
        return Flowable.fromIterable(
                getRestInterface().getRecords(
                        QueryGetRequest.builder()
                                .tableName("sys_variable_value")
                                .condition("document=sys_atf_step^variable.sys_name=Test script")
                                .build()))
                .map(it -> {
                    SnowScript script = getJsonManipulator().readFromJson(it);
                    JsonObject testStepRecord = getRestInterface().getRecord(
                            SingleRecordGetRequest.builder()
                                    .showDisplayValues(true)
                                    .tableName("sys_atf_step")
                                    .sysId(it.getObject("document_key").getString("value"))
                                    .build());
                    script.setScriptName(testStepRecord.getObject("test").getString("display_value") + "/" + testStepRecord.getString("order"));
                    script.setActive("true".equals(testStepRecord.getString("active")));
                    script.setCategory("automated-test");
                    return script;
                })
                .blockingIterable();
    }

    private JsonManipulator<SnowScript> getJsonManipulator() {
        return jsonManipulator;
    }
}
