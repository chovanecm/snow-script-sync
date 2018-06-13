package cz.chovanecm.snow.datalayer.rest;

import cz.chovanecm.snow.json.JsonManipulator;
import cz.chovanecm.snow.json.SnowScriptJsonManipulator;
import cz.chovanecm.snow.records.SnowScript;

public class SnowScriptRestDao extends GenericBaseRestDao<SnowScript> {

    private final SnowScriptJsonManipulator snowScriptManipulator;

    public SnowScriptRestDao(SnowRestInterface restInterface, String tableName, String scriptFieldName, String nameFieldName) {
        super(restInterface, tableName);
        snowScriptManipulator = new SnowScriptJsonManipulator(scriptFieldName, nameFieldName);
    }

    /**
     * Create default SnowScriptRestDao that assumes "script" is in the "script" field and "name" in the "name" field.
     *
     * @param restInterface
     * @param tableName
     */
    public SnowScriptRestDao(SnowRestInterface restInterface, String tableName) {
        super(restInterface, tableName);
        snowScriptManipulator = new SnowScriptJsonManipulator();
    }

    @Override
    protected JsonManipulator<SnowScript> getJsonManipulator() {
        return snowScriptManipulator;
    }
}
