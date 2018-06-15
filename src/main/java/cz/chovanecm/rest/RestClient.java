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

package cz.chovanecm.rest;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;

import static java.net.HttpURLConnection.HTTP_OK;
import static java.nio.charset.StandardCharsets.UTF_8;

public class RestClient {

    private CloseableHttpClient client;

    private String acceptHeader;

    public RestClient(String proxyHost, Integer proxyPort, String basicAuthUserName, String basicAuthPassword) {
        HttpClientBuilder builder = HttpClientBuilder.create();
        if (proxyHost != null && proxyPort != null) {
            builder.setProxy(new HttpHost(proxyHost, proxyPort));
            System.out.println("Using proxy: " + new HttpHost(proxyHost, proxyPort));
        }
        if (basicAuthPassword != null && basicAuthUserName != null) {
            BasicCredentialsProvider credProvider = new BasicCredentialsProvider();
            credProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(basicAuthUserName, basicAuthPassword));
            builder.setDefaultCredentialsProvider(credProvider);
        }
        client = builder.build();
    }

    public RestClient(String basicAuthUserName, String basicAuthPassword) {
        this(null, null, basicAuthUserName, basicAuthPassword);
    }

    public RestClient(String proxyHost, Integer proxyPort) {
        this(proxyHost, proxyPort, null, null);
    }

    public RestClient() {
        this(null, null, null, null);
    }

    public String getAcceptHeader() {
        return acceptHeader;
    }

    public void setAcceptHeader(String acceptHeader) {
        this.acceptHeader = acceptHeader;
    }

    public CloseableHttpResponse get(String url) throws IOException {
        HttpGet request = new HttpGet(url);
        if (getAcceptHeader() != null) {
            request.addHeader("Accept", getAcceptHeader());
        }
        return client.execute(request);
    }


    public void put(String url, String content) throws IOException, RestClientException {
        HttpPut request = new HttpPut(url);
        request.addHeader("Content-Type", getAcceptHeader());
        request.setEntity(new StringEntity(content, UTF_8));
        CloseableHttpResponse response = client.execute(request);
        if (response.getStatusLine().getStatusCode() != HTTP_OK) {
            throw new RestClientException(response.getStatusLine().toString());
        }
    }

    public static class RestClientException extends Exception {
        public RestClientException(String message) {
            super(message);
        }
    }
}
