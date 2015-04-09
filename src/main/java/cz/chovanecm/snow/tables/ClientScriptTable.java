package cz.chovanecm.snow.tables;

import cz.chovanecm.snow.json.ClientScriptJsonManipulator;
import cz.chovanecm.snow.json.JsonManipulator;

/**
 *
 * @author martin
 */
public class ClientScriptTable extends ScriptSnowTable{

    public ClientScriptTable() {
        super("sys_script_client", "script", "name");
    }

    @Override
    public JsonManipulator getJsonManipulator() {
        return new ClientScriptJsonManipulator(this);
    }
    
}
