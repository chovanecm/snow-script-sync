package cz.chovanecm.snow.api;

import com.github.jsonj.JsonArray;
import com.github.jsonj.JsonElement;
import com.github.jsonj.JsonObject;

import java.io.IOException;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JsonResultIterator implements Iterator<JsonObject> {

    private SnowClient snowClient;
    private SnowApiGetResponse response;
    private String nextUrl;
    private Iterator<JsonElement> iterator;

    public JsonResultIterator(SnowClient snowClient, SnowApiGetResponse response) throws IOException {
        this.snowClient = snowClient;
        this.response = response;
        nextUrl = response.getNextRecordsUrl();
        JsonObject body = response.getBody();
        JsonArray results = body.getArray("result");
        iterator = results.iterator();
    }

    @Override
    public boolean hasNext() {
        return nextUrl != null || iterator.hasNext();
    }

    @Override
    public JsonObject next() {
        if (!hasNext()) {
            return null;
        }
        if (!iterator.hasNext()) {
            try {
                System.out.println("Downloading: " + nextUrl);
                response = snowClient.get(nextUrl);
                nextUrl = response.getNextRecordsUrl();
                JsonObject body = response.getBody();
                JsonArray results = body.getArray("result");
                iterator = results.iterator();
            } catch (IOException ex) {
                Logger.getLogger(SnowClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        JsonElement element = iterator.next();
        return element.asObject();
    }
}
