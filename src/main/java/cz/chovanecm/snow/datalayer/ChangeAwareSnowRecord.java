package cz.chovanecm.snow.datalayer;

import cz.chovanecm.snow.records.SnowRecord;
import lombok.Getter;

import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * This class will remember changes that occurred on record attributes.
 */
public class ChangeAwareSnowRecord implements SnowRecord {
    @Getter
    private SnowRecord monitoredRecord;
    @Getter
    private Set<String> modifiedAttributes = new HashSet<>();

    public ChangeAwareSnowRecord(SnowRecord monitoredRecord) {
        this.setMonitoredRecord(monitoredRecord);
    }

    @Override
    public String setAttributeValue(String attribute, String value) {
        if (getAttributeValue(attribute) == null || !getAttributeValue(attribute).equals(value)) {
            modifiedAttributes.add(attribute);
        }
        return getMonitoredRecord().setAttributeValue(attribute, value);
    }

    @Override
    public String getCategory() {
        return getMonitoredRecord().getCategory();
    }

    @Override
    public void setCategory(String category) {
        getMonitoredRecord().setCategory(category);
    }

    public ChangeAwareSnowRecord setMonitoredRecord(SnowRecord monitoredRecord) {
        this.monitoredRecord = monitoredRecord;
        return this;
    }

    @Override
    public String getSysId() {
        return getMonitoredRecord().getSysId();
    }

    @Override
    public void setSysId(String sysId) {
        getMonitoredRecord().setSysId(sysId);
    }

    @Override
    public ZonedDateTime getUpdatedOn() {
        return getMonitoredRecord().getUpdatedOn();
    }

    @Override
    public void setUpdatedOn(ZonedDateTime updatedOn) {
        getMonitoredRecord().setUpdatedOn(updatedOn);
    }

    @Override
    public ZonedDateTime getCreatedOn() {
        return getMonitoredRecord().getCreatedOn();
    }

    @Override
    public void setCreatedOn(ZonedDateTime createdOn) {
        getMonitoredRecord().setCreatedOn(createdOn);
    }

    @Override
    public ActiveRecord getActiveRecord(ActiveRecordFactory factory) {
        return getMonitoredRecord().getActiveRecord(factory);
    }

    @Override
    public Set<String> getAttributes() {
        return getMonitoredRecord().getAttributes();
    }

    @Override
    public String getAttributeValue(String attribute) {
        return getMonitoredRecord().getAttributeValue(attribute);
    }
}
