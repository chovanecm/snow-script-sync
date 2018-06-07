package cz.chovanecm.snow.datalayer.rest.request;

import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class QueryGetRequest extends GetRequest {
    private String condition = "";

    @Builder
    public QueryGetRequest(String tableName, boolean showDisplayValues, String condition) {
        super(tableName, showDisplayValues);
        this.condition = condition;
    }

    @Override
    public String getResource() {
        return getTableName();
    }

    @Override
    public List<String> getParameters() {
        List<String> parameters = new ArrayList<>(super.getParameters());
        if (getCondition() != null) {
            parameters.add("sysparm_query=" + getCondition());
        }
        return parameters;
    }
}
