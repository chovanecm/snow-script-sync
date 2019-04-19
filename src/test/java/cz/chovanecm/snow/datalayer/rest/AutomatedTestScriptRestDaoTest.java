package cz.chovanecm.snow.datalayer.rest;


import cz.chovanecm.snow.datalayer.rest.dao.AutomatedTestScriptRestDao;
import cz.chovanecm.snow.datalayer.rest.request.QueryGetRequest;
import cz.chovanecm.snow.datalayer.rest.request.SingleRecordGetRequest;
import cz.chovanecm.snow.records.SnowScript;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class AutomatedTestScriptRestDaoTest extends RestTest {

    @Test
    public void getAll_shouldReturnClientScriptWithProperName() throws IOException {
        String expectedName = "TestDate/1";

        //GIVEN
        SnowRestInterface restInterface = mock(SnowRestInterface.class);
        AutomatedTestScriptRestDao instance = spy(new AutomatedTestScriptRestDao(restInterface));
        doReturn(restInterface).when(instance).getRestInterface();
        doReturn(
                Arrays.asList(readJsonObject("sys_variable_value-test.json"))
        ).when(restInterface).getRecords(any(QueryGetRequest.class));
        doReturn(readJsonObject("sys_aft_step.json")).when(restInterface).getRecord(SingleRecordGetRequest.builder()
                .showDisplayValues(true)
                .excludeReferenceLink(true)
                .tableName("sys_atf_step")
                .sysId("cfc6b8d20b10220050192f15d6673a40")
                .build());

        // WHEN
        Iterable<SnowScript> iterable = instance.getAll();
        SnowScript script = iterable.iterator().next();

        // THEN
        assertEquals(expectedName, script.getScriptName());
        assertEquals("// My great script!", script.getScript());
    }
}