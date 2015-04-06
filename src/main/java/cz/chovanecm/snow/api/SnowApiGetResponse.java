package cz.chovanecm.snow.api;

import com.github.jsonj.JsonObject;
import com.github.jsonj.tools.JsonParser;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.http.client.methods.CloseableHttpResponse;

/**
 *
 * @author Martin
 */
public class SnowApiGetResponse implements AutoCloseable{

    private CloseableHttpResponse response;

    public SnowApiGetResponse(CloseableHttpResponse response) {
        this.response = response;
    }

    public int getRowCount() {
        return Integer.parseInt(response.getFirstHeader("X-Total-Count").getValue());
    }

    /**
     * Returns URL pointing to next bunch of records. If there are no more records, returns null;
     * @return 
     */
    public String getNextRecordsUrl() {
        String links = response.getFirstHeader("Link").getValue();
        //https://demo018.service-now.com/api/now/v1/table/sys_script?sysparm_limit=1&sysparm_offset=0>;rel="first",<https://demo018.service-now.com/api/now/v1/table/sys_script?sysparm_limit=1&sysparm_offset=-1>;rel="prev",<https://demo018.service-now.com/api/now/v1/table/sys_script?sysparm_limit=1&sysparm_offset=1>;rel="next",<https://demo018.service-now.com/api/now/v1/table/sys_script?sysparm_limit=1&sysparm_offset=1353>;rel="last"
        Pattern pattern = Pattern.compile("<([^>]+)>;rel=\"next\"");
        Matcher matcher = pattern.matcher(links);
        if (matcher.find()) {
          return matcher.group(1);
        } else {
            return null;
        }
    }
    
    public JsonObject getBody() throws IOException {
        JsonObject object;
        try (InputStream is = response.getEntity().getContent()) {
            object = new JsonParser().parse(new BufferedReader(new InputStreamReader(is))).asObject();
        }
        return object;
    }

    @Override
    public void close() throws Exception {
        response.close();
    }
}
