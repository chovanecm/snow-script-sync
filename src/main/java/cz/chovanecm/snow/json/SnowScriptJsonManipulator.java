package cz.chovanecm.snow.json;

import com.github.jsonj.JsonObject;
import cz.chovanecm.snow.tables.ScriptSnowTable;
import cz.chovanecm.snow.records.SnowRecord;
import cz.chovanecm.snow.records.SnowScript;
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
         return setMyFields(json, new SnowScript(getTable()));
    }

    protected SnowRecord setMyFields(JsonObject json, SnowScript record) throws ParseException {
        super.setMyFields(json, record); 
        record.setScript(json.getString(getTable().getScriptField()));
        record.setScriptName(json.getString(getTable().getNameField()));
        record.setActive("true".equals(json.getString("active")));
        return record;
    }
    
    
    
}
