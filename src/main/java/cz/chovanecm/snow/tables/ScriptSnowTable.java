package cz.chovanecm.snow.tables;

import com.github.jsonj.JsonElement;
import com.github.jsonj.JsonObject;
import cz.chovanecm.snow.json.JsonManipulator;
import cz.chovanecm.snow.json.SnowScriptJsonManipulator;

/**
 *
 * @author Martin
 */
public class ScriptSnowTable extends SnowTable {
    private final String scriptField;
    private final String nameField;
    public ScriptSnowTable(String tableName, String scriptFieldName, String nameField) {
        super(tableName);
        this.scriptField = scriptFieldName;
        this.nameField = nameField;
    }


    public String getScriptField() {
        return scriptField;
    }

    public String getNameField() {
        return nameField;
    }

    @Override
    public JsonManipulator getJsonManipulator() {
        return new SnowScriptJsonManipulator(this);
    }

}
