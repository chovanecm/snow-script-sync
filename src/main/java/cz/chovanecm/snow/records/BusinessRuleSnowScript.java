package cz.chovanecm.snow.records;

import cz.chovanecm.snow.tables.SnowTable;

/**
 *
 * @author martin
 */
public class BusinessRuleSnowScript extends SnowScript {

    private String businessRuleOnTable = "";
    private String when = "";

    public BusinessRuleSnowScript(String sysId, String scriptName, String script, SnowTable table) {
        super(sysId, scriptName, script, table);
    }

    public BusinessRuleSnowScript(SnowTable table) {
        super(table);
    }

    public String getBusinessRuleOnTable() {
        return businessRuleOnTable;
    }

    public void setBusinessRuleOnTable(String businessRuleOnTable) {
        this.businessRuleOnTable = businessRuleOnTable;
    }

    public String getWhen() {
        return when;
    }

    public void setWhen(String when) {
        this.when = when;
    }

}
