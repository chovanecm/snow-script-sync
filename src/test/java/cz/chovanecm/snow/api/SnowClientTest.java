package cz.chovanecm.snow.api;

import cz.chovanecm.snow.SnowConnectorConfiguration;
import cz.chovanecm.snow.datalayer.rest.request.QueryGetRequest;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.io.IOException;

import static cz.chovanecm.snow.api.SnowClient.API_URL;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class SnowClientTest {

    @Test
    public void getRecords_urlIsBuildCorrectly() throws IOException {
        SnowConnectorConfiguration config = SnowConnectorConfiguration.builder().serviceNowDomainName("hello")
                .username("admin").password("password").build();
        SnowClient instance = spy(new SnowClient(config));
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        when(instance.getInstanceUrl()).thenReturn("https://my-instance");
        doReturn(mock(SnowApiGetResponse.class)).when(instance).get(anyString());
        doReturn(mock(JsonResultIterator.class)).when(instance).createIterator(any(SnowApiGetResponse.class));

        // GIVEN
        QueryGetRequest request = QueryGetRequest.builder().tableName("my_table").showDisplayValues(false).condition("sys_created_by=me").build();
        //WHEN
        instance.getRecords(request).iterator();

        // THEN
        verify(instance).get(captor.capture());
        assertEquals("https://my-instance" + API_URL + "my_table?sysparm_display_value=false&sysparm_exclude_reference_link=false&sysparm_query=sys_created_by%3Dme&sysparm_limit=100", captor.getValue());
    }
}