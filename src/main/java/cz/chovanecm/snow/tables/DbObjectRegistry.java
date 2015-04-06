package cz.chovanecm.snow.tables;

import cz.chovanecm.snow.records.DbObject;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author martin
 */
public class DbObjectRegistry {

    private Map<String, DbObject> sysIdToObject = new HashMap<>();
    private Map<String, DbObject> nameToObject = new HashMap<>();
    public DbObjectRegistry(Iterable<DbObject> dbObjects) {
        for (DbObject table : dbObjects) {
            sysIdToObject.put(table.getSysId(), table);
            nameToObject.put(table.getName(), table);
        }

        sysIdToObject.values().stream()
                .filter((table) -> (!"".equals(table.getSuperClassId())))
                .forEach((DbObject table) -> {
                    try {
                        sysIdToObject.get(table.getSuperClassId()).addChildObject(table);
                    } catch (NullPointerException ex) {
                        ex.printStackTrace();
                    }
                });
    }
    public DbObject getObjectBySysId(String sysId) {
        return sysIdToObject.get(sysId);
    }
    public DbObject getObjectByName(String name) {
        return nameToObject.get(name);
    }
    public Collection<DbObject> getAllObjects() {
        return sysIdToObject.values();
    }

}
