package cz.chovanecm.snow.datalayer.rest;

import com.github.jsonj.JsonObject;
import cz.chovanecm.snow.records.SnowRecord;
import cz.chovanecm.snow.tables.SnowTable;
import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;
import lombok.Getter;

import java.text.ParseException;

public abstract class GenericRestDao<T extends SnowRecord> implements cz.chovanecm.snow.datalayer.GenericDao<T> {
    @Getter
    private final SnowRestInterface restInterface;

    public GenericRestDao(SnowRestInterface restInterface) {
        this.restInterface = restInterface;
    }

    @Override
    public T get(String id) {
        try {
            JsonObject record = getRestInterface().getRecord(getTable().getTableName(), id);
            return getTable().getJsonManipulator().readFromJson(record);
        } catch (ParseException e) {
            //FIXME
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Iterable<T> getAll() {
        return Flowable.fromIterable(getRestInterface().getRecords(getTable().getTableName()))
                .observeOn(Schedulers.io())
                .map(it -> getTable().getJsonManipulator().readFromJson(it))
                .blockingIterable();
    }

    protected abstract SnowTable<T> getTable();
}
