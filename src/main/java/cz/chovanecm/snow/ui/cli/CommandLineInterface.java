/*
 * Snow Script Synchronizer is a tool helping developers to write scripts for ServiceNow
 *     Copyright (C) 2015-2017  Martin Chovanec <chovamar@fit.cvut.cz>
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.chovanecm.snow.ui.cli;

import cz.chovanecm.snow.SnowConnectorConfiguration;
import cz.chovanecm.snow.SnowScriptSynchronizer;
import cz.chovanecm.snow.ui.UserInterfaceException;
import org.apache.commons.cli.*;

import java.util.Arrays;

public class CommandLineInterface {
    private static final String[] MANDATORY_CLI_OPTIONS = {"d", "u", "i"};
    private CommandLine line;
    private static Options options = new Options();
    static {
        setupCommandLineOptions();
    }

    public static void main(String[] args) {
        try {
            CommandLineInterface userInterface = new CommandLineInterface(args);
            SnowConnectorConfiguration configuration = userInterface.getConnectorConfiguration();
            SnowScriptSynchronizer synchronizer = new SnowScriptSynchronizer(configuration,
                    userInterface.getDestinationFolder());
            synchronizer.downloadAll();
        } catch (UserInterfaceException e) {
            System.err.println(e.getMessage());
            CommandLineInterface.printHelp();
        }
    }

    public CommandLineInterface(String[] args) throws UserInterfaceException {
        try {
            line = new PosixParser().parse(options, args);
        } catch (ParseException e) {
            throw new UserInterfaceException(String.format("Error when parsing arguments %s. Cause: %s", Arrays.toString(args), e.getMessage()));
        }
        if (!mandatoryFieldsPresent()) {
            throw new UserInterfaceException("Destination, instance, and user are mandatory.");
        }
    }

    public String getDestinationFolder() {
        return getLine().getOptionValue("d");
    }


    public SnowConnectorConfiguration getConnectorConfiguration() throws UserInterfaceException {
        return buildProxyConfiguration(SnowConnectorConfiguration.builder())
                .serviceNowDomainName(getLine().getOptionValue("i"))
                .username(getLine().getOptionValue("u"))
                .password(getPassword())
                .build();

    }

    public String getPassword() {
        return String.copyValueOf(System.console().readPassword("Password:"));
    }

    private CommandLine getLine() {
        return line;
    }

    private SnowConnectorConfiguration.SnowConnectorConfigurationBuilder buildProxyConfiguration(SnowConnectorConfiguration.SnowConnectorConfigurationBuilder builder) throws UserInterfaceException {
        if (!getLine().hasOption("x")) {
            return builder;
        }
        String[] proxyString = getLine().getOptionValue("x").split(":");
        if (proxyString.length != 2) {
            throw new UserInterfaceException("Proxy format is host:port.");
        }
        try {
            return builder.proxyServerPort(Integer.parseInt(proxyString[1]))
                    .proxyServerAddress(proxyString[0]);
        } catch (NumberFormatException ex) {
            throw new UserInterfaceException("Proxy port number must be a number.");
        }
    }

    public static void printHelp() {
        new HelpFormatter().printHelp("_", options);
    }

    private boolean mandatoryFieldsPresent() {
        return Arrays.stream(MANDATORY_CLI_OPTIONS).allMatch(option -> getLine().hasOption(option));
    }

    private static void setupCommandLineOptions() {
        options.addOption("x", "proxy", true, "Use proxy, e.g. 10.0.0.1:3128");
        options.addOption("u", "user", true, "Use this user to connect to ServiceNow");
        options.addOption("d", "dest", true, "Where to download scripts");
        options.addOption("i", "instance", true, "Instance, e.g. demo019.service-now.com");
    }

}
