package cz.chovanecm.snow.datalayer.rest.dao;


public interface Filterable {
    String getQuery();

    void setQuery(String query);
}
