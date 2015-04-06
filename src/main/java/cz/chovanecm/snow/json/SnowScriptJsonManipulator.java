package cz.chovanecm.snow.json;

import com.github.jsonj.JsonObject;
import cz.chovanecm.snow.ScriptSnowTable;
import cz.chovanecm.snow.SnowRecord;
import cz.chovanecm.snow.SnowScript;
import java.text.ParseException;

/**
 *
 * @author martin
 */
public class SnowScriptJsonManipulator extends JsonManipulator{
    private final ScriptSnowTable table;

    public SnowScriptJsonManipulator(ScriptSnowTable table) {
        this.table = table;
    }

    public ScriptSnowTable getTable() {
        return table;
    }
    
    @Override
    public SnowRecord readFromJson(JsonObject json) throws ParseException {
         return setUpdated(json, new SnowScript(json.getString("sys_id"), json.getString(getTable().getNameField()), json.getString(getTable().getScriptField()), getTable()));
    }
    
}
