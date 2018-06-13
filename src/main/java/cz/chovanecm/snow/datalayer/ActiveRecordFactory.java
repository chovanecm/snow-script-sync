package cz.chovanecm.snow.datalayer;

import cz.chovanecm.snow.records.BusinessRuleSnowScript;
import cz.chovanecm.snow.records.ClientScript;
import cz.chovanecm.snow.records.SnowScript;

public interface ActiveRecordFactory {
    ActiveRecord getActiveRecordFor(BusinessRuleSnowScript businessRule);

    ActiveRecord getActiveRecordFor(ClientScript clientScript);

    ActiveRecord getActiveRecordFor(SnowScript snowScript);
}
