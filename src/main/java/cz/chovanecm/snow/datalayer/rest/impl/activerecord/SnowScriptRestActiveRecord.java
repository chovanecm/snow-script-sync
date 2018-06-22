package cz.chovanecm.snow.datalayer.rest.impl.activerecord;

import com.github.jsonj.JsonObject;
import cz.chovanecm.snow.datalayer.ActiveRecord;
import cz.chovanecm.snow.datalayer.rest.SnowRestInterface;
import cz.chovanecm.snow.records.SnowScript;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Builder
@Getter
public class SnowScriptRestActiveRecord implements ActiveRecord {
    /**
     * Target table name to store the record to. It expects "script" to be the field name by default.
     */
    @NonNull
    private String targetTable;
    /**
     * Field name that stores the actual script
     */
    @Builder.Default
    private String scriptFieldName = "script";
    @NonNull
    private SnowScript script;
    @NonNull
    private SnowRestInterface restInterface;


    @Override
    public void save() {
        getRestInterface().saveRecord(getTargetTable(),
                getScript().getSysId(),
                asJsonObject());
    }

    private JsonObject asJsonObject() {
        JsonObject json = new JsonObject();
        json.put(getScriptFieldName(), getScript().getScript());
        return json;
    }


}
