package cz.chovanecm.snow.datalayer.rest.request;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Getter
@EqualsAndHashCode(callSuper = true)
public class QueryGetRequest extends GetRequest {
    private final String condition;
    private final List<String> fieldsToReturn;

    @Builder
    public QueryGetRequest(String tableName, boolean showDisplayValues, boolean excludeReferenceLink, String condition, List<String> fieldsToReturn) {
        super(tableName, showDisplayValues, excludeReferenceLink);
        this.condition = condition;
        this.fieldsToReturn = fieldsToReturn;
    }

    @Override
    public String getResource() {
        return getTableName();
    }

    @Override
    public List<String> getParameters() {
        List<String> parameters = new ArrayList<>(super.getParameters());
        if (getCondition() != null) {
            parameters.add("sysparm_query=" + URLEncoder.encode(getCondition(), StandardCharsets.UTF_8));
        }
        if (getFieldsToReturn() != null && getFieldsToReturn().size() > 0) {
            parameters.add("sysparm_fields=" + String.join(",", getFieldsToReturn()));
        }
        return parameters;
    }
}
