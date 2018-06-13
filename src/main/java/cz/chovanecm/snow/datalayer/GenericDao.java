package cz.chovanecm.snow.datalayer;

public interface GenericDao<T> {
    T get(String id);

    Iterable<T> getAll();
}
