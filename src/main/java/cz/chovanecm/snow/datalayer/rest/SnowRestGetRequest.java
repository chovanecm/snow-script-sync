package cz.chovanecm.snow.datalayer.rest;

import lombok.Data;

@Data
public abstract class SnowRestGetRequest {
    private String tableName;
    private boolean showDisplayValues = false;

    public SnowRestGetRequest(String tableName, boolean showDisplayValues) {
        this.tableName = tableName;
        this.showDisplayValues = showDisplayValues;
    }

    public abstract String getResourcePath();
}
