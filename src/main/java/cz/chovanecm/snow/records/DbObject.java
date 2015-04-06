package cz.chovanecm.snow.records;


import cz.chovanecm.snow.tables.DbObjectTable;
import cz.chovanecm.snow.tables.SnowTable;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author martin
 */
public class DbObject extends SnowRecord {

    private String name = "";
    private String superClassId = "";
    private DbObject superClass;
    private final Set<DbObject> childs = new HashSet<>();
    
    public DbObject(String sysId) {
        super(new DbObjectTable(), sysId);
    }

    public DbObject() {
        super(new DbObjectTable());
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSuperClassId() {
        return superClassId;
    }

    public void setSuperClassId(String superClassId) {
        this.superClassId = superClassId;
    }

    public DbObject getSuperClass() {
        return superClass;
    }

    public void addChildObject(DbObject child) {
        childs.add(child);
        child.setSuperClass(this);
    }

    public void setSuperClass(DbObject superClass) {
        this.superClass = superClass;
    }

    public Set<DbObject> getChilds() {
        return childs;
    }
    
}
