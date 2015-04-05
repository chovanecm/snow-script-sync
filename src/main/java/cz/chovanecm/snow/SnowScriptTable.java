package cz.chovanecm.snow;

/**
 *
 * @author Martin
 */
public class SnowScriptTable {
    private final String tableName;
    private final String scriptField;
    private final String nameField;
    public SnowScriptTable(String tableName, String scriptFieldName, String nameField) {
        this.tableName = tableName;
        this.scriptField = scriptFieldName;
        this.nameField = nameField;
    }

    public String getTableName() {
        return tableName;
    }

    public String getScriptField() {
        return scriptField;
    }

    public String getNameField() {
        return nameField;
    }
    
}
