package cz.chovanecm.snow.datalayer.rest;

import cz.chovanecm.snow.datalayer.ActiveRecord;
import cz.chovanecm.snow.datalayer.ActiveRecordFactory;
import cz.chovanecm.snow.datalayer.rest.impl.activerecord.SnowScriptRestActiveRecord;
import cz.chovanecm.snow.records.SnowScript;
import lombok.Getter;

@Getter
public class RestActiveRecordFactory implements ActiveRecordFactory {

    private SnowRestInterface restInterface;

    public RestActiveRecordFactory(SnowRestInterface restInterface) {
        this.restInterface = restInterface;
    }

    @Override
    public ActiveRecord getActiveRecordForSnowScript(SnowScript snowScript) {
        return SnowScriptRestActiveRecord.builder().restInterface(getRestInterface())
                .script(snowScript)
                //TODO a better solution would be preferable
                .targetTable(snowScript.getCategory())
                .build();
    }
}
