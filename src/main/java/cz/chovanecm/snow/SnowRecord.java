package cz.chovanecm.snow;

import java.util.Date;

/**
 *
 * @author martin
 */
public abstract class SnowRecord {
    private SnowTable table;
    private String sysId;
    private Date updatedOn;
    private Date createdOn;

    public SnowRecord(SnowTable table, String sysId) {
        this.table = table;
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
    
}
