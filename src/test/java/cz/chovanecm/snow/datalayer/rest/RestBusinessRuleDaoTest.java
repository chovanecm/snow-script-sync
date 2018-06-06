package cz.chovanecm.snow.datalayer.rest;

import com.github.jsonj.JsonObject;
import com.github.jsonj.tools.JsonParser;
import cz.chovanecm.snow.records.BusinessRuleSnowScript;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class RestBusinessRuleDaoTest {
    private JsonParser parser = new JsonParser();

    @Test
    public void getAll_shouldReturnIterableOfBusinessRuleSnowScript() throws IOException {
        // GIVEN
        RestBusinessRuleDao instance = spy(new RestBusinessRuleDao(null));
        doReturn(mock(SnowRestInterface.class)).when(instance).getRestInterface();
        when(instance.getRestInterface().getAllRecords("sys_script")).thenReturn(
                Arrays.asList(
                        readJsonObject("sys_script_1.json"),
                        readJsonObject("sys_script_2.json")
                )
        );

        // WHEN
        Iterable<BusinessRuleSnowScript> iterable = instance.getAll();
        List<BusinessRuleSnowScript> scripts = new ArrayList<>();
        iterable.iterator().forEachRemaining(scripts::add);

        // THEN
        assertEquals(2, scripts.size());
        assertEquals("new IdentificationRuleValidator(current.sys_id).validate();", scripts.get(0).getScript());
        assertEquals("//we do nothing :-) new IdentificationRuleValidator(current.sys_id).validate();", scripts.get(1).getScript());
    }

    public JsonObject readJsonObject(String resourceName) throws IOException {
        return parser.parse(getResource(resourceName)).asObject();
    }

    public InputStreamReader getResource(String resourceName) {
        return new InputStreamReader(getClass().getResourceAsStream("/cz.chovanecm.snow.datalayer.rest/" + resourceName));
    }

}