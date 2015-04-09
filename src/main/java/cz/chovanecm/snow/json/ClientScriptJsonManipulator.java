package cz.chovanecm.snow.json;

import com.github.jsonj.JsonObject;
import cz.chovanecm.snow.records.ClientScript;
import cz.chovanecm.snow.records.SnowRecord;
import cz.chovanecm.snow.records.SnowScript;
import cz.chovanecm.snow.tables.ScriptSnowTable;
import java.text.ParseException;

/**
 *
 * @author martin
 */
public class ClientScriptJsonManipulator extends SnowScriptJsonManipulator {

    public ClientScriptJsonManipulator(ScriptSnowTable table) {
        super(table);
    }

    @Override
    public SnowRecord readFromJson(JsonObject json) throws ParseException {
        return this.setMyFields(json, new ClientScript(getTable()));
    }

    
    protected SnowRecord setMyFields(JsonObject json, ClientScript record) throws ParseException {
        super.setMyFields(json, record);
        record.setTableName(json.getString("table"));
        return record;
    }

}
