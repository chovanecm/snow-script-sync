package cz.chovanecm.snow.datalayer;

import cz.chovanecm.snow.records.SnowScript;

public interface AutomatedTestScriptDao {
    Iterable<SnowScript> getAll();
}
