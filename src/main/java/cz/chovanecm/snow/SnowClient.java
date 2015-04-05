package cz.chovanecm.snow;

import com.github.jsonj.JsonArray;
import com.github.jsonj.JsonElement;
import com.github.jsonj.JsonObject;
import cz.chovanecm.rest.RestClient;
import cz.chovanecm.snow.api.SnowApiGetResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

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

    public String getTableApiUrl(SnowScriptTable table) {
        return getApiUrl() + table.getTableName();
    }

    public Iterable<SnowScript> readAll(SnowScriptTable sourceTable, int readsPerRequest) throws IOException {
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

    public class ResultIterator implements Iterator<SnowScript> {

        private SnowApiGetResponse response;
        private String nextUrl;
        private Iterator<JsonElement> iterator;
        private SnowScriptTable table;
        private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
        public ResultIterator(SnowScriptTable table, SnowApiGetResponse response) throws IOException {
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
        public SnowScript next() {
            if (!hasNext()) {
                return null;
            }
            if (!iterator.hasNext()) {
                try {
                    System.out.println("Downloading: " + nextUrl);
                    response = new SnowApiGetResponse(client.get(nextUrl));
                    System.out.println("Downloaded.");
                    nextUrl = response.getNextRecordsUrl();
                    JsonArray results = response.getBody().getArray("result");
                    iterator = results.iterator();
                } catch (IOException ex) {
                    Logger.getLogger(SnowClient.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            JsonElement element = iterator.next();
            JsonObject object = element.asObject();
            SnowScript snowScript = new SnowScript(object.getString("sys_id"), object.getString(table.getNameField()), object.getString(table.getScriptField()), table);
            try {
                
                snowScript.setUpdated(dateFormat.parse(object.getString("sys_updated_on") + " GMT"));
            } catch (ParseException ex) {
                snowScript.setUpdated(new Date());
                Logger.getLogger(SnowClient.class.getName()).log(Level.SEVERE, null, ex);
            }
            return snowScript;
        }

    }

}
