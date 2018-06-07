package cz.chovanecm.snow.datalayer.rest.request;

import lombok.Data;

import java.util.Arrays;
import java.util.List;

@Data
public abstract class GetRequest {
    private String tableName;
    private boolean showDisplayValues = false;

    public GetRequest(String tableName, boolean showDisplayValues) {
        this.tableName = tableName;
        this.showDisplayValues = showDisplayValues;
    }

    /**
     * Get relative path to the resource.
     *
     * @return Returns e.g. myTable or myTable/abc123
     */
    public abstract String getResource();

    /**
     * Get URL parameters
     *
     * @return URL parameters. format [x=1, y=2]
     */
    public List<String> getParameters() {
        return Arrays.asList(String.format("sysparm_display_value=%s", isShowDisplayValues()));
    }
}
