package cz.chovanecm.snow.datalayer.rest;

import com.github.jsonj.JsonObject;
import com.github.jsonj.tools.JsonParser;

import java.io.IOException;
import java.io.InputStreamReader;

public abstract class RestTest {
    public JsonObject readJsonObject(String resourceName) throws IOException {
        JsonParser parser = new JsonParser();
        return parser.parse(getResource(resourceName)).asObject();
    }

    public InputStreamReader getResource(String resourceName) {
        return new InputStreamReader(getClass().getResourceAsStream("/cz.chovanecm.snow.datalayer.rest/" + resourceName));
    }
}
