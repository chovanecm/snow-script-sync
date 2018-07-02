package cz.chovanecm.snow.ui.cli;


import cz.chovanecm.snow.SnowConnectorConfiguration;
import cz.chovanecm.snow.ui.UserInterfaceException;
import org.apache.commons.cli.ParseException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

public class CommandLineParserTest {

    @ParameterizedTest
    @ValueSource(strings = {"",
            "-i tlx.com -u user",
            "-d dir -i tlx.com",
            "-i tlx.com",})
    public void newInstance_shouldThrowException_whenMandatoryFieldsAreMissing(String commandLine) {
        Assertions.assertThrows(UserInterfaceException.class, () -> new CommandLineInterface(commandLine.split(" ")));
    }

    @Test
    public void getConnectorConfiguration_shouldReturnFullConfiguration_whenInstanceUserPasswordAndProxySpecified() throws UserInterfaceException, ParseException {
        // GIVEN
        String commandLine = "-d dir -i tlx.com -u user1 -x proxyHost.com:1234";
        CommandLineParser cli = spy(new CommandLineParser());
        doReturn("password2").when(cli).getPassword();
        cli.parse(commandLine.split(" "));
        // WHEN
        SnowConnectorConfiguration configuration = cli.getConnectorConfiguration();

        // THEN
        assertEquals("user1", configuration.getUsername());
        assertEquals("password2", configuration.getPassword());
        assertEquals("proxyHost.com", configuration.getProxyServerAddress());
        assertEquals(1234, configuration.getProxyServerPort());
        assertTrue(configuration.isProxySet());
    }

    @Test
    public void getConnectorConfiguration_shouldReturnConfigurationWithoutProxy_whenInstanceUserPasswordSpecified() throws UserInterfaceException, ParseException {
        // GIVEN
        String commandLine = "-d dir -i tlx.com -u user1";
        CommandLineParser cli = spy(new CommandLineParser());
        doReturn("password2").when(cli).getPassword();
        cli.parse(commandLine.split(" "));

        // WHEN
        SnowConnectorConfiguration configuration = cli.getConnectorConfiguration();

        // THEN
        assertEquals("user1", configuration.getUsername());
        assertEquals("password2", configuration.getPassword());
        assertNull(configuration.getProxyServerAddress());
        assertFalse(configuration.isProxySet());
    }
}