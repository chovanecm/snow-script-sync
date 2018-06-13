package cz.chovanecm.snow.datalayer.rest.request;

import lombok.Builder;
import lombok.Getter;

import java.util.Objects;


@Getter
public class SingleRecordGetRequest extends GetRequest {
    private String sysId;

    @Builder
    public SingleRecordGetRequest(String tableName, boolean showDisplayValues, String sysId) {
        super(tableName, showDisplayValues);
        this.sysId = sysId;
    }

    @Override
    public String getResource() {
        return String.format("%s/%s", getTableName(), getSysId());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        SingleRecordGetRequest that = (SingleRecordGetRequest) o;
        return Objects.equals(sysId, that.sysId);
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), sysId);
    }
}
