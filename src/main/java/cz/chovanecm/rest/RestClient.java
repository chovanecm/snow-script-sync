package cz.chovanecm.rest;

import java.io.IOException;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

/**
 *
 * @author Martin
 */
public class RestClient {

    private CloseableHttpClient client;

    private String acceptHeader;
    
    public RestClient(String proxyHost, Integer proxyPort, String basicAuthUserName, String basicAuthPassword) {
        HttpClientBuilder builder = HttpClientBuilder.create();
        if (proxyHost != null && proxyPort != null) {
            builder.setProxy(new HttpHost(proxyHost, proxyPort));
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
        System.out.println("Executing req");
       
        
        return client.execute(request);
    }

    
}
