package cz.chovanecm.snow.records;

import cz.chovanecm.snow.RecordAccessor;
import cz.chovanecm.snow.tables.SnowTable;
import java.io.IOException;

/**
 *
 * @author martin
 */
public class ClientScript extends SnowScript implements TableBasedObject{

    private String tableName;

    
    public ClientScript(String sysId, String scriptName, String script, SnowTable table) {
        super(sysId, scriptName, script, table);
    }

    public ClientScript(SnowTable table) {
        super(table);
    }

    @Override
    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    @Override
    public void save(RecordAccessor destination) throws IOException {
        destination.saveClientScript(this);
    }
    
}
