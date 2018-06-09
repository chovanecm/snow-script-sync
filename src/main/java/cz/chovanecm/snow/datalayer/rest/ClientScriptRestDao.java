package cz.chovanecm.snow.datalayer.rest;

import cz.chovanecm.snow.json.ClientScriptJsonManipulator;
import cz.chovanecm.snow.json.JsonManipulator;
import cz.chovanecm.snow.records.ClientScript;

public class ClientScriptRestDao extends GenericBaseRestDao<ClientScript> {

    public ClientScriptRestDao(SnowRestInterface restInterface) {
        super(restInterface, "sys_script_client");
    }

    @Override
    protected JsonManipulator<ClientScript> getJsonManipulator() {
        return new ClientScriptJsonManipulator();
    }
}
