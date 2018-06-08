package cz.chovanecm.snow.datalayer.rest;

import cz.chovanecm.snow.records.SnowScript;
import cz.chovanecm.snow.tables.ScriptSnowTable;
import cz.chovanecm.snow.tables.SnowTable;

public class GenericScriptRestDao extends GenericRestDao<SnowScript> {
    private ScriptSnowTable table;

    public GenericScriptRestDao(SnowRestInterface restInterface, String tableName, String scriptFieldName, String nameFieldName) {
        super(restInterface);
        table = new ScriptSnowTable(tableName, scriptFieldName, nameFieldName);
    }

    /**
     * Create default GenericScriptRestDao that assumes "script" is in the "script" field and "name" in the "name" field.
     *
     * @param restInterface
     * @param tableName
     */
    public GenericScriptRestDao(SnowRestInterface restInterface, String tableName) {
        this(restInterface, tableName, "script", "name");
    }

    @Override
    protected SnowTable<SnowScript> getTable() {
        return table;
    }
}
