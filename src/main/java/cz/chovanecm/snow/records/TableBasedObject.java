package cz.chovanecm.snow.records;

/**
 * This interface is used for objects that are based on some table, such as business rules
 * @author martin
 */
public interface TableBasedObject {

    /**
     * Returns table name 
     * @return
     */
    public String getTableName();
    
}
