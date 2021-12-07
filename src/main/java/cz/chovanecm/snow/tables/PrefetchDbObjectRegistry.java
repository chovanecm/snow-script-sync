package cz.chovanecm.snow.tables;

import cz.chovanecm.snow.datalayer.rest.dao.DbObjectDao;
import cz.chovanecm.snow.records.DbObject;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import lombok.Getter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PrefetchDbObjectRegistry implements DbObjectRegistry {
    @Getter
    private final DbObjectDao dao;
    private final Map<String, DbObject> nameToObject = new ConcurrentHashMap<>();
    private final Map<String, DbObject> sysIdToObject = new ConcurrentHashMap<>();

    public PrefetchDbObjectRegistry(DbObjectDao dao) {
        this.dao = dao;
        Flowable.fromIterable(getDao().getAll())
                .subscribeOn(Schedulers.io())
                .doOnNext(this::insertTableToRegistry)
                .subscribe();
    }

    @Override
    public DbObject getObjectByName(String name) {

        if (!nameToObject.containsKey(name)) {
            DbObject table = getDao().findTableByTableName(name);
            insertTableToRegistry(table);
        }
        return nameToObject.get(name);
    }

    public DbObject getObjectById(String sysId) throws IOException {
        if (!sysIdToObject.containsKey(sysId)) {
            DbObject table = getDao().get(sysId);
            insertTableToRegistry(table);
        }
        return sysIdToObject.get(sysId);
    }

    private void insertTableToRegistry(DbObject table) {
        if (nameToObject.containsKey(table.getName())) {
            return;
        }
        nameToObject.put(table.getName(), table);
        sysIdToObject.put(table.getSysId(), table);

        if (table.hasSuperClass()) {
            try {
                DbObject parentTable = getObjectById(table.getSuperClassId());
                table.setSuperClass(parentTable);
                insertTableToRegistry(parentTable);
            } catch (IOException e) {
                System.err.println("Could not find parent table for table " + table.getName());
            }
        }
    }


}
