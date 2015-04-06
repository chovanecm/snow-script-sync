package cz.chovanecm.snow.tables;

import cz.chovanecm.snow.json.BusinessRuleJsonManipulator;
import cz.chovanecm.snow.json.JsonManipulator;

/**
 *
 * @author martin
 */
public class BusinessRuleTable extends ScriptSnowTable{

    public BusinessRuleTable() {
        super("sys_script", "script", "name");
    }

    @Override
    public JsonManipulator getJsonManipulator() {
        return new BusinessRuleJsonManipulator(this);
    }
    
}
