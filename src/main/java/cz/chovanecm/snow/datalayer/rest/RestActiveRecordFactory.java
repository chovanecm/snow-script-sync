package cz.chovanecm.snow.datalayer.rest;

import cz.chovanecm.snow.datalayer.ActiveRecord;
import cz.chovanecm.snow.datalayer.ActiveRecordFactory;
import cz.chovanecm.snow.datalayer.rest.impl.activerecord.BusinessRuleActiveRecord;
import cz.chovanecm.snow.records.BusinessRuleSnowScript;
import cz.chovanecm.snow.records.ClientScript;
import cz.chovanecm.snow.records.SnowScript;
import lombok.Getter;

@Getter
public class RestActiveRecordFactory implements ActiveRecordFactory {

    private SnowRestInterface restInterface;

    public RestActiveRecordFactory(SnowRestInterface restInterface) {
        this.restInterface = restInterface;
    }

    @Override
    public ActiveRecord getActiveRecordFor(BusinessRuleSnowScript businessRule) {
        return new BusinessRuleActiveRecord(businessRule, getRestInterface());
    }

    @Override
    public ActiveRecord getActiveRecordFor(ClientScript clientScript) {
        return null;
    }

    @Override
    public ActiveRecord getActiveRecordFor(SnowScript snowScript) {
        return null;
    }
}
