package cz.chovanecm.snow.datalayer.rest.request;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@Getter
@EqualsAndHashCode(callSuper = true)
public class QueryGetRequest extends GetRequest {
    private String condition = "";

    @Builder
    public QueryGetRequest(String tableName, boolean showDisplayValues, boolean excludeReferenceLink, String condition) {
        super(tableName, showDisplayValues, excludeReferenceLink);
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
            try {
                parameters.add("sysparm_query=" + URLEncoder.encode(getCondition(), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("Wrong encoding? Developer's fault! ", e);
            }
        }
        return parameters;
    }
}
