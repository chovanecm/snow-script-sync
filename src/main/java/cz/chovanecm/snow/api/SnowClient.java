/*
 * Snow Script Synchronizer is a tool helping developers to write scripts for ServiceNow
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

import com.github.jsonj.JsonObject;
import cz.chovanecm.rest.RestClient;
import cz.chovanecm.snow.SnowConnectorConfiguration;
import cz.chovanecm.snow.datalayer.rest.SnowRestInterface;
import cz.chovanecm.snow.datalayer.rest.request.QueryGetRequest;
import cz.chovanecm.snow.datalayer.rest.request.SingleRecordGetRequest;
import cz.chovanecm.snow.records.SnowRecord;
import cz.chovanecm.snow.tables.SnowTable;

import java.io.IOException;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SnowClient implements SnowRestInterface {

    private final static String API_URL = "/api/now/v1/table/";
    private final RestClient client;
    private final String instanceUrl;
    private final int readsPerRequest = 100;

    public SnowClient(String instanceUrl, String basicAuthUserName, String basicAuthPassword, String proxyHost, Integer proxyPort) {
        client = new RestClient(proxyHost, proxyPort, basicAuthUserName, basicAuthPassword);
        client.setAcceptHeader("application/json;charset=utf-8");
        this.instanceUrl = instanceUrl;
    }

    public SnowClient(SnowConnectorConfiguration connectorConfiguration) {
        this("https://" + connectorConfiguration.getServiceNowDomainName(),
                connectorConfiguration.getUsername(),
                connectorConfiguration.getPassword(),
                connectorConfiguration.isProxySet() ? connectorConfiguration.getProxyServerAddress() : null,
                connectorConfiguration.isProxySet() ? connectorConfiguration.getProxyServerPort() : null);
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

    public <T extends SnowRecord> Iterable<T> readAll(SnowTable sourceTable, int readsPerRequest, Class<T> type) {
        return () -> {
            try {
                SnowApiGetResponse response = get(getTableApiUrl(sourceTable) + "?sysparm_limit=" + readsPerRequest);
                return new ResultIterator<T>(this, sourceTable, response);
            } catch (IOException ex) {
                Logger.getLogger(SnowClient.class.getName()).log(Level.SEVERE, null, ex);
                return Collections.emptyIterator();
            }
        };
    }

    public SnowApiGetResponse get(String url) throws IOException {
        return new SnowApiGetResponse(client.get(url));
    }

    public <T extends SnowRecord> T getRecordBySysId(SnowTable sourceTable, String sysId, Class<T> type) throws IOException {
        SnowApiGetResponse response = get(getTableApiUrl(sourceTable) + "?sysparm_query=sys_id=" + sysId);
        ResultIterator<T> iterator = new ResultIterator<T>(this, sourceTable, response);
        return iterator.next();
    }

    @Override
    public Iterable<JsonObject> getRecords(QueryGetRequest request) {
        return () -> {
            try {
                SnowApiGetResponse response = get(API_URL + request.getResource()
                        + "?sysparm_limit" + String.join("&", request.getParameters()));
                return new JsonResultIterator(this, response);
            } catch (IOException e) {
                //FIXME
                e.printStackTrace();
                return Collections.emptyIterator();
            }

        };

    }

    @Override
    public JsonObject getRecord(SingleRecordGetRequest request) {
        try {
            SnowApiGetResponse response = get(API_URL + request.getResource() + "?" + String.join("&", request.getParameters()));
            return response.getBody().getObject("result");
        } catch (IOException e) {
            //FIXME
            e.printStackTrace();
            return null;
        }
    }
}
