package cz.chovanecm.snow.api;

import cz.chovanecm.snow.records.SnowRecord;
import cz.chovanecm.snow.tables.SnowTable;
import com.github.jsonj.JsonArray;
import com.github.jsonj.JsonElement;
import com.github.jsonj.JsonObject;
import cz.chovanecm.rest.RestClient;
import java.io.IOException;
import java.text.ParseException;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.client.methods.CloseableHttpResponse;

/**
 *
 * @author Martin
 */
public class SnowClient {

    private final RestClient client;
    private final String instanceUrl;
    private final static String API_URL = "/api/now/v1/table/";

    public SnowClient(String instanceUrl, String basicAuthUserName, String basicAuthPassword, String proxyHost, Integer proxyPort) {
        client = new RestClient(proxyHost, proxyPort, basicAuthUserName, basicAuthPassword);
        client.setAcceptHeader("application/json;charset=utf-8");
        this.instanceUrl = instanceUrl;
    }

    public String getInstanceUrl() {
        return instanceUrl;
    }

    public String getApiUrl() {
        return getInstanceUrl() + API_URL;
    }

    public String getTableApiUrl(SnowTable table) {
        return getApiUrl() + table.getTableName();
    }

    public <T extends SnowRecord> Iterable<T> readAll(SnowTable sourceTable, int readsPerRequest, Class<T> type) throws IOException {
        SnowApiGetResponse response = new SnowApiGetResponse(client.get(getTableApiUrl(sourceTable) + "?sysparm_limit=" + readsPerRequest));
        return () -> {
            try {
                return new ResultIterator(sourceTable, response);
            } catch (IOException ex) {
                Logger.getLogger(SnowClient.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        };
    }

    public <T extends SnowRecord> T getRecordBySysId(SnowTable sourceTable, String sysId, Class<T> type) throws IOException {
        CloseableHttpResponse httpResponse = client.get(getTableApiUrl(sourceTable) + "?sysparm_query=sys_id=" + sysId);
        SnowApiGetResponse response = new SnowApiGetResponse(httpResponse);
        ResultIterator<T> iterator = new ResultIterator(sourceTable, response);
        return iterator.next();
    }

    public class ResultIterator<T extends SnowRecord> implements Iterator<T> {

        private SnowApiGetResponse response;
        private String nextUrl;
        private Iterator<JsonElement> iterator;
        private SnowTable table;

        public ResultIterator(SnowTable table, SnowApiGetResponse response) throws IOException {
            this.response = response;
            this.table = table;
            nextUrl = response.getNextRecordsUrl();
            JsonArray results = response.getBody().getArray("result");
            iterator = results.iterator();
        }

        @Override
        public boolean hasNext() {
            return nextUrl != null || iterator.hasNext();
        }

        @Override
        public T next() {
            if (!hasNext()) {
                return null;
            }
            if (!iterator.hasNext()) {
                try {
                    System.out.println("Downloading: " + nextUrl);
                    response = new SnowApiGetResponse(client.get(nextUrl));
                    nextUrl = response.getNextRecordsUrl();
                    JsonArray results = response.getBody().getArray("result");
                    iterator = results.iterator();
                } catch (IOException ex) {
                    Logger.getLogger(SnowClient.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            JsonElement element = iterator.next();
            JsonObject object = element.asObject();
            try {
                return (T) table.getJsonManipulator().readFromJson(object);
            } catch (ParseException ex) {
                Logger.getLogger(SnowClient.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }

    }

}
