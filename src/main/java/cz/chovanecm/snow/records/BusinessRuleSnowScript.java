package cz.chovanecm.snow.records;

import cz.chovanecm.snow.RecordAccessor;
import cz.chovanecm.snow.tables.SnowTable;
import java.io.IOException;

/**
 *
 * @author martin
 */
public class BusinessRuleSnowScript extends SnowScript implements TableBasedObject {

    private String tableName = "";
    private String when = "";
    
    public BusinessRuleSnowScript(String sysId, String scriptName, String script, SnowTable table) {
        super(sysId, scriptName, script, table);
    }

    public BusinessRuleSnowScript(SnowTable table) {
        super(table);
    }

    @Override
    public String getTableName() {
        return tableName;
    }

    public void setTableName(String businessRuleOnTable) {
        this.tableName = businessRuleOnTable;
    }

    public String getWhen() {
        return when;
    }

    public void setWhen(String when) {
        this.when = when;
    }

    @Override
    public void save(RecordAccessor destination) throws IOException {
        destination.saveBusinessRule(this);
    }

}
