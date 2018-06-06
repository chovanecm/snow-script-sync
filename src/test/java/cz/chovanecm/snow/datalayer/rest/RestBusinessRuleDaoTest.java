package cz.chovanecm.snow.datalayer.rest;

import cz.chovanecm.snow.records.BusinessRuleSnowScript;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class RestBusinessRuleDaoTest extends RestDaoTest {

    @Test
    public void getAll_shouldReturnIterableOfBusinessRuleSnowScript() throws IOException {
        // GIVEN
        BusinessRuleRestDao instance = spy(new BusinessRuleRestDao(null));
        doReturn(mock(SnowRestInterface.class)).when(instance).getRestInterface();
        when(instance.getRestInterface().getRecords("sys_script")).thenReturn(
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

}