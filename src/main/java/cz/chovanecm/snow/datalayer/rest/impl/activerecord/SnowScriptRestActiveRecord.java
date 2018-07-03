package cz.chovanecm.snow.datalayer.rest.impl.activerecord;

import com.google.gson.JsonObject;
import cz.chovanecm.snow.datalayer.ActiveRecord;
import cz.chovanecm.snow.datalayer.rest.SnowRestInterface;
import cz.chovanecm.snow.records.SnowScript;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import java.util.function.Function;

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

    private final Function<String, Function<String, Function<String, String>>> replace = (String search) -> (String replacement) -> (String str) -> str.replace(search, replacement);

    private JsonObject asJsonObject() {
        com.google.gson.JsonObject element = new com.google.gson.JsonObject();
        element.addProperty(getScriptFieldName(), getScript().getScript());

        return element;
    }


}
