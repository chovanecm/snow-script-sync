package cz.chovanecm.snow.json;

import cz.chovanecm.snow.records.SnowScript;

public class SnowScriptJsonManipulator extends AbstractSnowScriptJsonManipulator<SnowScript> {
    public SnowScriptJsonManipulator(String scriptFieldName, String nameFieldName) {
        super(scriptFieldName, nameFieldName);
    }

    public SnowScriptJsonManipulator() {
    }

    @Override
    protected SnowScript initializeEmptyRecord() {
        return new SnowScript();
    }
}
