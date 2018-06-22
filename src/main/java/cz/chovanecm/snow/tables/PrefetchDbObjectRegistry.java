package cz.chovanecm.snow.tables;

import cz.chovanecm.snow.datalayer.rest.dao.DbObjectDao;
import cz.chovanecm.snow.records.DbObject;
import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;
import lombok.Getter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PrefetchDbObjectRegistry implements DbObjectRegistry {
    @Getter
    private DbObjectDao dao;
    private Map<String, DbObject> nameToObject = new ConcurrentHashMap<>();
    private Map<String, DbObject> sysIdToObject = new ConcurrentHashMap<>();

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

    public DbObject getObjectById(String sysId) {
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
            DbObject parentTable = getObjectById(table.getSuperClassId());
            table.setSuperClass(parentTable);
            insertTableToRegistry(parentTable);
        }
    }


}
