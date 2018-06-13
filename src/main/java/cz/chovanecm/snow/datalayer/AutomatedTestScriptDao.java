package cz.chovanecm.snow.datalayer;

import cz.chovanecm.snow.records.SnowScript;

public interface AutomatedTestScriptDao extends GenericDao<SnowScript> {
    @Override
    Iterable<SnowScript> getAll();
}
