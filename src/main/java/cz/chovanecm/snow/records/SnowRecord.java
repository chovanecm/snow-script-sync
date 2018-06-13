package cz.chovanecm.snow.records;

import cz.chovanecm.snow.datalayer.ActiveRecord;
import cz.chovanecm.snow.datalayer.ActiveRecordFactory;

public interface SnowRecord {
    String getSysId();

    java.time.ZonedDateTime getUpdatedOn();

    java.time.ZonedDateTime getCreatedOn();

    default ActiveRecord getActiveRecord(ActiveRecordFactory factory) {
        return () -> System.err.println("Save not implemented for " + getClass().getCanonicalName());
    }
}
