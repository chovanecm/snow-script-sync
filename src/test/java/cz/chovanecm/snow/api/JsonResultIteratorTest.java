package cz.chovanecm.snow.api;

import com.github.jsonj.JsonObject;
import cz.chovanecm.snow.datalayer.rest.RestTest;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

public class JsonResultIteratorTest extends RestTest {
    @Test
    public void testIterator() throws IOException {
        // GIVEN
        SnowClient client = mock(SnowClient.class);
        SnowApiGetResponse response1 = mock(SnowApiGetResponse.class);
        SnowApiGetResponse response2 = mock(SnowApiGetResponse.class);
        doReturn(readJsonObject("sys_variable_value-list.json")).when(response1).getBody();
        doReturn("http://next-url").when(response1).getNextRecordsUrl();
        doReturn(readJsonObject("sys_variable_value-list2.json")).when(response2).getBody();
        doReturn(response2).when(client).get("http://next-url");
        JsonResultIterator instance = new JsonResultIterator(client, response1);

        // WHEN
        List<JsonObject> results = new ArrayList<>();
        instance.forEachRemaining(results::add);

        // THEN
        assertEquals(20, results.size());
        assertEquals("000439a2eb113200c2b9666cd206fe4e", results.get(0).getString("sys_id"));
        assertEquals("02afe6374a362327003346a0c9e45f08", results.get(19).getString("sys_id"));
    }
}