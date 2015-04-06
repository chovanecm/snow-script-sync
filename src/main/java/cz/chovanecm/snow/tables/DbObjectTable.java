package cz.chovanecm.snow.tables;

import cz.chovanecm.snow.json.DbObjectJsonManipulator;
import cz.chovanecm.snow.json.JsonManipulator;

/**
 *
 * @author martin
 */
public class DbObjectTable extends SnowTable{

    public DbObjectTable() {
        super("sys_db_object");
    }

    @Override
    public JsonManipulator getJsonManipulator() {
        return new DbObjectJsonManipulator();
    }
    
}
