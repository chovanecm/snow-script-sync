package cz.chovanecm.snow.datalayer.rest;

import cz.chovanecm.snow.records.ClientScript;
import cz.chovanecm.snow.tables.ClientScriptTable;
import cz.chovanecm.snow.tables.SnowTable;

public class ClientScriptRestDao extends GenericRestDao<ClientScript> {
    private final ClientScriptTable table;

    public ClientScriptRestDao(SnowRestInterface restInterface) {
        super(restInterface);
        this.table = new ClientScriptTable();
    }

    @Override
    protected SnowTable<ClientScript> getTable() {
        return table;
    }
}
