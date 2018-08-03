package cz.chovanecm.snow.datalayer.rest;

import cz.chovanecm.snow.datalayer.SnowVariable;
import cz.chovanecm.snow.datalayer.rest.dao.SnowRecordScriptRestDao;
import cz.chovanecm.snow.records.SnowRecord;
import lombok.Getter;

public class RestSnowVariable implements SnowVariable {

    @Getter
    private final SnowRecord variableRecord;
    @Getter
    private SnowRestInterface restInterface;

    public RestSnowVariable(SnowRestInterface restInterface, SnowRecord variableRecord) {
        this.restInterface = restInterface;
        this.variableRecord = variableRecord;
    }

    @Override
    public SnowRecord getRelatedRecord() {
        return new SnowRecordScriptRestDao(getRestInterface(), getDocument()).get(getDocumentKey());
    }

    private String getDocument() {
        return getVariableRecord().getAttributeValue("document");
    }

    private String getDocumentKey() {
        return getVariableRecord().getAttributeValue("document_key");
    }
}
