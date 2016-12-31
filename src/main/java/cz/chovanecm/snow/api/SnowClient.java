/*
 * Snow Script Synchroniser is a tool helping developers to write scripts for ServiceNow
 *     Copyright (C) 2015-2017  Martin Chovanec <chovamar@fit.cvut.cz>
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

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
