package cz.chovanecm.snow.datalayer.rest.dao;

import com.github.jsonj.JsonObject;
import cz.chovanecm.snow.datalayer.rest.SnowRestInterface;
import cz.chovanecm.snow.json.JsonManipulator;
import cz.chovanecm.snow.records.AbstractSnowRecord;
import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;
import lombok.Getter;

@Getter
public abstract class GenericBaseRestDao<T extends AbstractSnowRecord> implements cz.chovanecm.snow.datalayer.GenericDao<T> {
    private final SnowRestInterface restInterface;
    private final String tableName;

    public GenericBaseRestDao(SnowRestInterface restInterface, String tableName) {
        this.restInterface = restInterface;
        this.tableName = tableName;
    }

    @Override
    public T get(String id) {
        JsonObject record = getRestInterface().getRecord(getTableName(), id);
        return getJsonManipulator().readFromJson(record);
    }

    @Override
    public Iterable<T> getAll() {
        return Flowable.fromIterable(getRestInterface().getRecords(getTableName()))
                .observeOn(Schedulers.io())
                .map(it -> getJsonManipulator().readFromJson(it))
                .doOnNext(it -> it.setCategory(getTableName()))
                .blockingIterable();
    }

    protected abstract JsonManipulator<T> getJsonManipulator();
}
