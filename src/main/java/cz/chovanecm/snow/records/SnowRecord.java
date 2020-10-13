package cz.chovanecm.snow.records;

import cz.chovanecm.snow.datalayer.ActiveRecord;
import cz.chovanecm.snow.datalayer.ActiveRecordFactory;

import java.util.Set;

public interface SnowRecord {
    String getSysId();

    java.time.ZonedDateTime getUpdatedOn();

    String getUpdatedBy();

    void setUpdatedBy(String updatedBy);

    java.time.ZonedDateTime getCreatedOn();

    default ActiveRecord getActiveRecord(ActiveRecordFactory factory) {
        return () -> System.err.println("Save not implemented for " + getClass().getCanonicalName());
    }

    Set<String> getAttributes();

    String getAttributeValue(String attribute);

    String setAttributeValue(String attribute, String value);

    String getCategory();

    void setCategory(String category);

    void setSysId(String sysId);

    void setUpdatedOn(java.time.ZonedDateTime updatedOn);

    void setCreatedOn(java.time.ZonedDateTime createdOn);

}
