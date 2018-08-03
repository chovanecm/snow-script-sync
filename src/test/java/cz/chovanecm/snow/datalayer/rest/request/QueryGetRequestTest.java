package cz.chovanecm.snow.datalayer.rest.request;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class QueryGetRequestTest {

    @Test
    public void getParameters_shouldReturnConditionInQuery() {
        QueryGetRequest instance = QueryGetRequest.builder().tableName("table").condition("sys_created_by=me^time=now").build();
        assertTrue(instance.getParameters().contains("sysparm_query=sys_created_by%3Dme%5Etime%3Dnow"));
    }

    @Test
    public void getParameters_displayValueShouldBeFalseByDefault() {
        QueryGetRequest instance = QueryGetRequest.builder().tableName("table").condition("sys_created_by=me^time=now").build();
        assertTrue(instance.getParameters().contains("sysparm_display_value=false"));
    }


    @Test
    public void getParameters_displayValueShouldBeTrue_whenSet() {
        QueryGetRequest instance = QueryGetRequest.builder().tableName("table").condition("sys_created_by=me^time=now").showDisplayValues(true).build();
        System.out.println(instance.getParameters());
        assertTrue(instance.getParameters().contains("sysparm_display_value=true"));
    }

    @Test
    public void getParameters_conditionShouldBeEmptyWhenNotSet() {
        QueryGetRequest instance = QueryGetRequest.builder().tableName("table").showDisplayValues(true).build();
        assertFalse(instance.getParameters().stream().anyMatch(it -> it.contains("sys_parm_query=")));
    }
}