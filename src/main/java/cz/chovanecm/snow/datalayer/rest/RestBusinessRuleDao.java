package cz.chovanecm.snow.datalayer.rest;

import cz.chovanecm.snow.datalayer.BusinessRuleDao;
import cz.chovanecm.snow.records.BusinessRuleSnowScript;
import cz.chovanecm.snow.tables.BusinessRuleTable;
import io.reactivex.Flowable;

import java.text.ParseException;

public class RestBusinessRuleDao implements BusinessRuleDao {
    private final BusinessRuleTable TABLE = new BusinessRuleTable();
    SnowRestInterface restInterface;

    public RestBusinessRuleDao(SnowRestInterface restInterface) {
        this.restInterface = restInterface;
    }

    @Override
    public BusinessRuleSnowScript get(String id) {
        try {
            return TABLE.getJsonManipulator().readFromJson(getRestInterface().getRecord(TABLE.getTableName(), id));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //FIXME throw an exception
        return null;
    }

    @Override
    public Iterable<BusinessRuleSnowScript> getAll() {
        return Flowable.fromIterable(getRestInterface().getAllRecords(TABLE.getTableName()))
                .map(it -> TABLE.getJsonManipulator().readFromJson(it))
                .blockingIterable();
    }

    public SnowRestInterface getRestInterface() {
        return restInterface;
    }

}
