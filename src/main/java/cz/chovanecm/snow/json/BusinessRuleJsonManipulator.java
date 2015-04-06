/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.chovanecm.snow.json;

import com.github.jsonj.JsonObject;
import cz.chovanecm.snow.records.BusinessRuleSnowScript;
import cz.chovanecm.snow.tables.ScriptSnowTable;
import cz.chovanecm.snow.records.SnowRecord;
import java.text.ParseException;

/**
 *
 * @author martin
 */
public class BusinessRuleJsonManipulator extends SnowScriptJsonManipulator {

    public BusinessRuleJsonManipulator(ScriptSnowTable table) {
        super(table);
    }

    @Override
    public SnowRecord readFromJson(JsonObject json) throws ParseException {
        return this.setMyFields(json, new BusinessRuleSnowScript(getTable()));
    }
    
    protected BusinessRuleSnowScript setMyFields(JsonObject json, BusinessRuleSnowScript script) throws ParseException {
        super.setMyFields(json, script);
        script.setBusinessRuleOnTable(json.getString("collection"));
        script.setWhen(json.getString("when"));
        return script;
    }
    
}
