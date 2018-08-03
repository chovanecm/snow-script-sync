package cz.chovanecm.snow.datalayer.rest.impl.activerecord;

import com.google.gson.JsonObject;
import cz.chovanecm.snow.datalayer.ActiveRecord;
import cz.chovanecm.snow.datalayer.ChangeAwareSnowRecord;
import cz.chovanecm.snow.datalayer.rest.SnowRestInterface;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Builder
public class ChangeAwareRestActiveRecord implements ActiveRecord {
    @NonNull
    @Getter
    private SnowRestInterface restInterface;
    @Getter
    @NonNull
    private ChangeAwareSnowRecord record;

    public ChangeAwareRestActiveRecord(SnowRestInterface restInterface, ChangeAwareSnowRecord record) {
        this.restInterface = restInterface;
        this.record = record;
    }

    @Override
    public void save() {
        System.out.println("Saving " + asJsonObject().toString());
        getRestInterface().saveRecord(getRecord().getCategory(),
                getRecord().getSysId(),
                asJsonObject());
    }

    private JsonObject asJsonObject() {
        JsonObject element = new JsonObject();

        getRecord().getModifiedAttributes()
                .forEach(attribute -> element.addProperty(attribute, getRecord().getAttributeValue(attribute)));

        return element;
    }
}
