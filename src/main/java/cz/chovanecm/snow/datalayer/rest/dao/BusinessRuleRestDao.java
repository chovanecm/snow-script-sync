package cz.chovanecm.snow.datalayer.rest.dao;

import cz.chovanecm.snow.datalayer.BusinessRuleDao;
import cz.chovanecm.snow.datalayer.rest.SnowRestInterface;
import cz.chovanecm.snow.json.BusinessRuleJsonManipulator;
import cz.chovanecm.snow.json.JsonManipulator;
import cz.chovanecm.snow.records.BusinessRuleSnowScript;

public class BusinessRuleRestDao extends GenericBaseRestDao<BusinessRuleSnowScript> implements BusinessRuleDao {

    public BusinessRuleRestDao(SnowRestInterface restInterface) {
        super(restInterface, "sys_script");
    }

    @Override
    protected JsonManipulator<BusinessRuleSnowScript> getJsonManipulator() {
        return new BusinessRuleJsonManipulator();
    }
}
