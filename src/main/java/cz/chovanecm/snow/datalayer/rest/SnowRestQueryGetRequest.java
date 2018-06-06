package cz.chovanecm.snow.datalayer.rest;

import lombok.Builder;

public class SnowRestQueryGetRequest extends SnowRestGetRequest {
    private String condition = "";

    @Builder
    public SnowRestQueryGetRequest(String tableName, boolean showDisplayValues, String condition) {
        super(tableName, showDisplayValues);
        this.condition = condition;
    }

    @Override
    public String getResourcePath() {
        //FIXME
        return null;
    }
}
