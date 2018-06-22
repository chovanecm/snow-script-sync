package cz.chovanecm.snow.datalayer;

import cz.chovanecm.snow.records.SnowScript;
import cz.chovanecm.snow.records.TableAwareSnowScript;

public interface ActiveRecordFactory {
    default ActiveRecord getActiveRecordForTableAwareSnowScript(TableAwareSnowScript tableAwareSnowScript) {
        return getActiveRecordForSnowScript(tableAwareSnowScript);
    }

    ActiveRecord getActiveRecordForSnowScript(SnowScript snowScript);
}
