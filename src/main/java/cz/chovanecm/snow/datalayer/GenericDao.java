package cz.chovanecm.snow.datalayer;

import java.io.IOException;

public interface GenericDao<T> {
    T get(String id) throws IOException;

    Iterable<T> getAll();
}
