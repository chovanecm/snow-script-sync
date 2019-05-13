package cz.chovanecm.snow.datalayer.rest.dao;

import com.github.jsonj.JsonObject;
import cz.chovanecm.snow.datalayer.rest.SnowRestInterface;
import cz.chovanecm.snow.datalayer.rest.request.QueryGetRequest;
import cz.chovanecm.snow.json.JsonManipulator;
import cz.chovanecm.snow.records.SnowRecord;
import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;
import lombok.Getter;
import lombok.Setter;

import java.util.function.Supplier;

@Getter
public abstract class GenericBaseRestDao<T extends SnowRecord> implements cz.chovanecm.snow.datalayer.GenericDao<T>, Filterable {
    private final SnowRestInterface restInterface;
    private final String tableName;
    @Setter
    private String query = "";
    @Setter
    private Supplier<String> categoryNameSupplier = this::getTableName;

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
        return Flowable.fromIterable(getRestInterface()
                .getRecords(QueryGetRequest.builder()
                        .tableName(getTableName())
                        .excludeReferenceLink(true)
                        .condition(getQuery()).build()))
                .observeOn(Schedulers.io())
                .map(getJsonManipulator()::readFromJson)
                .doOnNext(it -> it.setCategory(getCategoryNameSupplier().get()))
                .blockingIterable();
    }

    protected abstract JsonManipulator<T> getJsonManipulator();
}
