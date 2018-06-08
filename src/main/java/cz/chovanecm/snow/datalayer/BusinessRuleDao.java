package cz.chovanecm.snow.datalayer;


import cz.chovanecm.snow.records.BusinessRuleSnowScript;

public interface BusinessRuleDao extends GenericDao<BusinessRuleSnowScript> {

    @Override
    BusinessRuleSnowScript get(String id);

    @Override
    Iterable<BusinessRuleSnowScript> getAll();
}
