package cz.chovanecm.snow.datalayer.rest;

import cz.chovanecm.snow.datalayer.rest.dao.TableAwareScriptRestDao;
import cz.chovanecm.snow.records.TableAwareSnowScript;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class TableAwareScriptRestDaoTest extends RestTest {

    @Test
    public void getAll_shouldReturnIterableOfTableAwareSnowScript() throws IOException {
        // GIVEN
        TableAwareScriptRestDao instance = spy(new TableAwareScriptRestDao(null, "sys_script", "collection"));
        doReturn(mock(SnowRestInterface.class)).when(instance).getRestInterface();
        when(instance.getRestInterface().getRecords("sys_script")).thenReturn(
                Arrays.asList(
                        readJsonObject("sys_script_1.json"),
                        readJsonObject("sys_script_2.json")
                )
        );

        // WHEN
        Iterable<TableAwareSnowScript> iterable = instance.getAll();
        List<TableAwareSnowScript> scripts = new ArrayList<>();
        iterable.iterator().forEachRemaining(scripts::add);

        // THEN
        assertEquals(2, scripts.size());
        assertEquals("new IdentificationRuleValidator(current.sys_id).validate();", scripts.get(0).getScript());
        assertEquals("//we do nothing :-) new IdentificationRuleValidator(current.sys_id).validate();", scripts.get(1).getScript());
    }

}