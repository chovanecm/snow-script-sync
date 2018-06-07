package cz.chovanecm.snow.datalayer.rest;


import cz.chovanecm.snow.datalayer.rest.request.QueryGetRequest;
import cz.chovanecm.snow.datalayer.rest.request.SingleRecordGetRequest;
import cz.chovanecm.snow.records.SnowScript;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class AutomatedTestScriptRestTest extends RestTest {

    @Test
    public void getAll_shouldReturnClientScriptWithProperName() throws IOException {
        String expectedName = "TestDate/1";

        //GIVEN
        AutomatedTestScriptRestDao instance = spy(new AutomatedTestScriptRestDao());
        doReturn(mock(SnowRestInterface.class)).when(instance).getRestInterface();
        when(instance.getRestInterface().getRecords(any(QueryGetRequest.class))).thenReturn(
                Arrays.asList(readJsonObject("sys_variable_value-test.json"))
        );
        when(instance.getRestInterface().getRecord(SingleRecordGetRequest.builder()
                .showDisplayValues(true)
                .tableName("sys_atf_step")
                .sysId("cfc6b8d20b10220050192f15d6673a40")
                .build()))
                .thenReturn(readJsonObject("sys_aft_step.json"));

        // WHEN
        Iterable<SnowScript> iterable = instance.getAll();
        SnowScript script = iterable.iterator().next();

        // THEN
        assertEquals(expectedName, script.getScriptName());
        assertEquals("// My great script!", script.getScript());
    }
}