package cz.chovanecm.snow.json;

import com.github.jsonj.JsonObject;
import cz.chovanecm.snow.tables.ScriptSnowTable;
import cz.chovanecm.snow.records.SnowRecord;
import cz.chovanecm.snow.records.SnowScript;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Set;
import java.util.function.Consumer;
import org.apache.commons.lang.ArrayUtils;

/**
 *
 * @author martin
 */
public class SnowScriptJsonManipulator extends JsonManipulator {

    private final ScriptSnowTable table;

    public SnowScriptJsonManipulator(ScriptSnowTable table) {
        this.table = table;
    }

    public ScriptSnowTable getTable() {
        return table;
    }

    @Override
    public SnowRecord readFromJson(JsonObject json) throws ParseException {
        return setMyFields(json, new SnowScript(getTable()));
    }

    protected SnowRecord setMyFields(JsonObject json, SnowScript record) throws ParseException {
        super.setMyFields(json, record);

        record.setScript(json.getString(getTable().getScriptField()));
        record.setScriptName(json.getString(getTable().getNameField()));
        record.setActive("true".equals(json.getString("active")));
        Set<String> attributes = json.keySet();
        attributes.forEach((String attribute) -> {
            //Skip script field - we already have it.
            //NOTE: Perhaps we should store it anyway and just exclude it when e.g. storing as file?
            if (!attribute.equals(getTable().getScriptField())) {
                if (json.get(attribute).isPrimitive()) {
                    record.setAttributeValue(attribute, json.getString(attribute));
                } else {
                    record.setAttributeValue(attribute, "[sss:debug:non-primitive-value]");
                }
            }
        });
        return record;
    }

}
