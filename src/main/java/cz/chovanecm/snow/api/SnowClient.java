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
import com.google.gson.Gson;
import cz.chovanecm.rest.RestClient;
import cz.chovanecm.snow.SnowConnectorConfiguration;
import cz.chovanecm.snow.datalayer.rest.SnowRestInterface;
import cz.chovanecm.snow.datalayer.rest.request.QueryGetRequest;
import cz.chovanecm.snow.datalayer.rest.request.SingleRecordGetRequest;
import lombok.Getter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

@Getter
public class SnowClient implements SnowRestInterface {

    public static final String API_URL = "/api/now/v2/table/";
    private final RestClient client;
    private final String instanceUrl;
    private final int readsPerRequest = 100;

    public SnowClient(String instanceUrl, String basicAuthUserName, String basicAuthPassword, String proxyHost, Integer proxyPort) {
        client = new RestClient(proxyHost, proxyPort, basicAuthUserName, basicAuthPassword);
        getClient().setAcceptHeader("application/json;charset=utf-8");
        this.instanceUrl = instanceUrl;
    }

    public SnowClient(SnowConnectorConfiguration connectorConfiguration) {
        this("https://" + connectorConfiguration.getServiceNowDomainName(),
                connectorConfiguration.getUsername(),
                connectorConfiguration.getPassword(),
                connectorConfiguration.isProxySet() ? connectorConfiguration.getProxyServerAddress() : null,
                connectorConfiguration.isProxySet() ? connectorConfiguration.getProxyServerPort() : null);
    }


    public String getApiUrl() {
        return getInstanceUrl() + API_URL;
    }


    public SnowApiGetResponse get(String url) throws IOException {
        try {
            System.out.println("Downloading: " + url);
            return new SnowApiGetResponse(getClient().get(url));
        } catch (IOException ex) {
            throw new IOException("IO Exception while downloading URL " + url, ex);
        }

    }

    @Override
    public Iterable<JsonObject> getRecords(QueryGetRequest request) {
        return () -> {
            try {
                ArrayList<String> parameters = new ArrayList<>(request.getParameters());
                parameters.add("sysparm_limit=" + readsPerRequest);
                SnowApiGetResponse response = get(getApiUrl() + request.getResource()
                        + "?" + String.join("&", parameters));
                return createIterator(response);
            } catch (IOException e) {
                //FIXME
                e.printStackTrace();
                return Collections.emptyIterator();
            }

        };

    }

    JsonResultIterator createIterator(SnowApiGetResponse response) throws IOException {
        return new JsonResultIterator(this, response);
    }

    @Override
    public JsonObject getRecord(SingleRecordGetRequest request) {
        try {
            SnowApiGetResponse response = get(getApiUrl() + request.getResource() + "?" + String.join("&", request.getParameters()));
            return response.getBody().getObject("result");
        } catch (IOException e) {
            //FIXME
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void saveRecord(String table, String sysId, com.google.gson.JsonObject object) {
        String endpoint = getApiUrl() + table + "/" + sysId;
        try {
            getClient().patch(endpoint, new Gson().toJson(object));
        } catch (IOException | RestClient.RestClientException e) {
            //FIXME
            e.printStackTrace();
        }
    }

}
