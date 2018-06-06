package cz.chovanecm.snow.datalayer.rest;

import lombok.Builder;
import lombok.Getter;

import java.util.Objects;


@Getter
public class SingleRecordSnowRestGetGetRequest extends SnowRestGetRequest {
    private String sysId;

    @Builder
    public SingleRecordSnowRestGetGetRequest(String tableName, boolean showDisplayValues, String sysId) {
        super(tableName, showDisplayValues);
        this.sysId = sysId;
    }

    @Override
    public String getResourcePath() {
        //FIXME
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        SingleRecordSnowRestGetGetRequest that = (SingleRecordSnowRestGetGetRequest) o;
        return Objects.equals(sysId, that.sysId);
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), sysId);
    }
}
