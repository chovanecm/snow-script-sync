package cz.chovanecm.snow.files;

import cz.chovanecm.snow.records.DbObject;
import cz.chovanecm.snow.records.DeactivableSnowRecord;
import cz.chovanecm.snow.records.TableBasedObject;
import cz.chovanecm.snow.tables.DbObjectRegistry;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 *
 * @author martin
 */
public class DirectoryTreeBuilder {
    private final DbObjectRegistry objectRegistry;

    public DirectoryTreeBuilder(DbObjectRegistry objectRegistry) {
        this.objectRegistry = objectRegistry;
    }

    public DbObjectRegistry getObjectRegistry() {
        return objectRegistry;
    }
    
    /**
     * Returns path based on table according to table extensions, e.g. cmdb_ci/cmdb_ci_hardware/cmdb_ci_computer
     * @param tableBasedObject
     * @return 
     */
    public Path getPathForTableBasedObject(TableBasedObject tableBasedObject) {
        DbObject object = getObjectRegistry().getObjectByName(tableBasedObject.getTableName());
        if (object == null) {
            return Paths.get(".");
        }
        Path path = Paths.get(object.getName());
        while (object.getSuperClass() != null) {
            object = object.getSuperClass();
            path = Paths.get(object.getName()).resolve(path);
        }
        return path;
    }
    
    public Path getPathForDeactivableSnowRecord(DeactivableSnowRecord record) {
        return (record.isActive() ? Paths.get(".") : Paths.get("_inactive_"));
    }
}
