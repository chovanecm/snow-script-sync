package cz.chovanecm.snow.datalayer.rest.dao;

import cz.chovanecm.snow.datalayer.rest.SnowRestInterface;
import cz.chovanecm.snow.json.JsonManipulator;
import cz.chovanecm.snow.json.TableAwareSnowScriptManipulator;
import cz.chovanecm.snow.records.TableAwareSnowScript;
import lombok.Getter;

import java.util.function.Supplier;

public class TableAwareScriptRestDao extends GenericBaseRestDao<TableAwareSnowScript> {

    @Getter
    private final String assignedTableField;

    private final Supplier<JsonManipulator<TableAwareSnowScript>> jsonManipulatorSupplier;
    /**
     * Access Table aware script - a script that is only relevant for a particular table
     *
     * @param restInterface
     * @param restTable          The table that stores the script (e.g. sys_script)
     * @param assignedTableField The element in the record that stores the name of the record that the script belongs to
     */
    public TableAwareScriptRestDao(SnowRestInterface restInterface, String restTable, String assignedTableField) {
        super(restInterface, restTable);
        this.assignedTableField = assignedTableField;
        jsonManipulatorSupplier = () -> new TableAwareSnowScriptManipulator(getAssignedTableField());
    }

    public TableAwareScriptRestDao(SnowRestInterface restInterface, String restTable, String scriptFieldName, String nameFieldName, String assignedTableField) {
        super(restInterface, restTable);
        this.assignedTableField = assignedTableField;
        jsonManipulatorSupplier = () -> new TableAwareSnowScriptManipulator(scriptFieldName, nameFieldName, getAssignedTableField());
    }

    @Override
    protected JsonManipulator<TableAwareSnowScript> getJsonManipulator() {
        return jsonManipulatorSupplier.get();
    }
}
