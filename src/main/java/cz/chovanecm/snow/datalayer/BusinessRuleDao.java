package cz.chovanecm.snow.datalayer;


import cz.chovanecm.snow.records.BusinessRuleSnowScript;

public interface BusinessRuleDao {
    BusinessRuleSnowScript get(String id);

    Iterable<BusinessRuleSnowScript> getAll();
}
