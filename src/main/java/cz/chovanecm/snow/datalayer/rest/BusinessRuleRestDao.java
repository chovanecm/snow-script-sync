package cz.chovanecm.snow.datalayer.rest;

import cz.chovanecm.snow.datalayer.BusinessRuleDao;
import cz.chovanecm.snow.records.BusinessRuleSnowScript;
import cz.chovanecm.snow.tables.BusinessRuleTable;
import cz.chovanecm.snow.tables.SnowTable;

public class BusinessRuleRestDao extends GenericRestDao<BusinessRuleSnowScript> implements BusinessRuleDao {
    private final BusinessRuleTable table = new BusinessRuleTable();

    public BusinessRuleRestDao(SnowRestInterface restInterface) {
        super(restInterface);
    }

    @Override
    protected SnowTable<BusinessRuleSnowScript> getTable() {
        return table;
    }
}
