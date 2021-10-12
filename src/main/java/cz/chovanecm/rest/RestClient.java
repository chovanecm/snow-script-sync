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

import java.io.IOException;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static java.net.HttpURLConnection.HTTP_OK;

public class RestClient {


    private final HttpClient client;
    private String acceptHeader;

    public RestClient(String proxyHost, Integer proxyPort, String basicAuthUserName, String basicAuthPassword) {
        var builder = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .followRedirects(HttpClient.Redirect.NORMAL);

        if (proxyHost != null && proxyPort != null) {
            builder = builder.proxy(ProxySelector.of(new InetSocketAddress(proxyHost, proxyPort)));
            System.out.printf("Using proxy: %s:%d%n", proxyHost, proxyPort);
        }
        if (basicAuthPassword != null && basicAuthUserName != null) {
            builder = builder.authenticator(new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(basicAuthUserName, basicAuthPassword.toCharArray());
                }
            });
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

    public HttpResponse<String> get(String url) throws IOException {
        try {
            System.out.printf("GET %s%n", url);
            return client.send(HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Accept", this.getAcceptHeader())
                    .GET().build(), HttpResponse.BodyHandlers.ofString());
        } catch (InterruptedException e) {
            throw new IOException(e);
        }
    }


    public void patch(String url, String content) throws IOException, RestClientException {
        try {
            var response = client.send(HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", this.getAcceptHeader())
                    .method("PATCH", HttpRequest.BodyPublishers.ofString(content)).build(), HttpResponse.BodyHandlers.ofString());
            System.out.printf("PATCH %s%n", url);
            if (response.statusCode() != HTTP_OK) {
                throw new RestClientException(String.valueOf(response.statusCode()));
            }
        } catch (InterruptedException e) {
            throw new IOException(e);
        }


    }

    public static class RestClientException extends Exception {
        public RestClientException(String message) {
            super(message);
        }
    }
}
