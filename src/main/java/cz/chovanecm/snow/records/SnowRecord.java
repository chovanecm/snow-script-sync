package cz.chovanecm.snow.records;

import cz.chovanecm.snow.RecordAccessor;
import cz.chovanecm.snow.tables.SnowTable;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author martin
 */
public abstract class SnowRecord {
    private final SnowTable table;
    private String sysId;
    private Date updatedOn;
    private Date createdOn;
    private final Map<String, String> attributes = new HashMap<>();
    public SnowRecord(SnowTable table, String sysId) {
        this.table = table;
        this.sysId = sysId;
    }

    public SnowRecord(SnowTable table) {
        this.table = table;
    }
    
    public void setSysId(String sysId) {
        this.sysId = sysId;
    }

    public SnowTable getTable() {
        return table;
    }

    public String getSysId() {
        return sysId;
    }

    public Date getUpdatedOn() {
        return updatedOn;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setUpdatedOn(Date updatedOn) {
        this.updatedOn = updatedOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }
    public Set<String> getAttributes() {
        return this.attributes.keySet();
    }
    public String getAttributeValue(String attribute) {
        return this.attributes.get(attribute);
    }

    public String setAttributeValue(String attribute, String value) {
        return attributes.put(attribute, value);
    }
    
    public abstract void save(RecordAccessor destination) throws IOException;
    
}
