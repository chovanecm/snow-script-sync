package cz.chovanecm.snow.datalayer.rest;

import cz.chovanecm.snow.datalayer.BusinessRuleDao;
import cz.chovanecm.snow.records.BusinessRuleSnowScript;
import cz.chovanecm.snow.tables.BusinessRuleTable;
import io.reactivex.Flowable;

import java.text.ParseException;

public class RestBusinessRuleDao implements BusinessRuleDao {
    private final BusinessRuleTable table = new BusinessRuleTable();
    SnowRestInterface restInterface;

    public RestBusinessRuleDao(SnowRestInterface restInterface) {
        this.restInterface = restInterface;
    }

    @Override
    public BusinessRuleSnowScript get(String id) {
        try {
            return table.getJsonManipulator().readFromJson(getRestInterface().getRecord(table.getTableName(), id));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //FIXME throw an exception
        return null;
    }

    @Override
    public Iterable<BusinessRuleSnowScript> getAll() {
        return Flowable.fromIterable(getRestInterface().getAllRecords(table.getTableName()))
                .map(it -> table.getJsonManipulator().readFromJson(it))
                .blockingIterable();
    }

    public SnowRestInterface getRestInterface() {
        return restInterface;
    }

}
