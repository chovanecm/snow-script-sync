package cz.chovanecm.snow.datalayer.rest.dao;

import com.github.jsonj.JsonObject;
import cz.chovanecm.snow.datalayer.AutomatedTestScriptDao;
import cz.chovanecm.snow.datalayer.rest.SnowRestInterface;
import cz.chovanecm.snow.datalayer.rest.request.QueryGetRequest;
import cz.chovanecm.snow.datalayer.rest.request.SingleRecordGetRequest;
import cz.chovanecm.snow.json.JsonManipulator;
import cz.chovanecm.snow.json.SnowScriptJsonManipulator;
import cz.chovanecm.snow.records.SnowScript;
import io.reactivex.Flowable;
import lombok.Getter;
import lombok.Setter;

public class AutomatedTestScriptRestDao implements AutomatedTestScriptDao, Filterable {
    private final SnowScriptJsonManipulator jsonManipulator = new SnowScriptJsonManipulator("value", "sys_id");
    @Getter
    @Setter
    private String query = "";

    public AutomatedTestScriptRestDao(SnowRestInterface restInterface) {
        this.restInterface = restInterface;
    }

    @Getter
    SnowRestInterface restInterface;

    @Override
    public SnowScript get(String id) {
        return jsonObjectToSnowScript(getRestInterface().getRecord(SingleRecordGetRequest.builder()
                .tableName("sys_variable_value")
                .sysId(id)
                .showDisplayValues(false)
                .excludeReferenceLink(true)
                .build()));
    }

    @Override
    public Iterable<SnowScript> getAll() {
        return Flowable.fromIterable(readAllTests())
                .map(this::jsonObjectToSnowScript)
                .blockingIterable();
    }

    public SnowScript jsonObjectToSnowScript(JsonObject object) {
        SnowScript script = getJsonManipulator().readFromJson(object);
        JsonObject testStepRecord = getRestInterface().getRecord(
                SingleRecordGetRequest.builder()
                        .showDisplayValues(true)
                        .excludeReferenceLink(true)
                        .tableName("sys_atf_step")
                        .sysId(object.getString("document_key"))
                        .build());
        script.setScriptName(testStepRecord.getString("test") + "/" + testStepRecord.getString("order"));
        script.setActive("true".equals(testStepRecord.getString("active")));
        script.setCategory("automated-test");
        return script;
    }

    public Iterable<JsonObject> readAllTests() {
        return getRestInterface().getRecords(
                QueryGetRequest.builder()
                        .tableName("sys_variable_value")
                        .excludeReferenceLink(true)
                        .condition("document=sys_atf_step^variable.sys_name=Test script^" + getQuery())
                        .build());
    }


    private JsonManipulator<SnowScript> getJsonManipulator() {
        return jsonManipulator;
    }
}
