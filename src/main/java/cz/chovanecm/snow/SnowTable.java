package cz.chovanecm.snow;

import cz.chovanecm.snow.json.JsonManipulator;

/**
 *
 * @author martin
 */
public abstract class SnowTable {
    protected final String tableName;

    public SnowTable(String tableName) {
        this.tableName = tableName;
    }

    public String getTableName() {
        return tableName;
    }
    public abstract JsonManipulator getJsonManipulator();
}
