package cz.chovanecm.snow.ui.cli;

import cz.chovanecm.snow.SnowConnectorConfiguration;
import cz.chovanecm.snow.ui.TaskVariables;
import cz.chovanecm.snow.ui.UserInterfaceException;
import lombok.Getter;
import org.apache.commons.cli.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Not thread-safe
 */
public class CommandLineParser {
    static final String[] MANDATORY_CLI_OPTIONS = {"d", "u", "i"};
    @Getter
    private Options options = new Options();
    @Getter
    private CommandLine line;

    public CommandLineParser() {
        setupCommandLineOptions();

    }

    public TaskVariables parse(String[] args) throws ParseException, UserInterfaceException {
        line = new PosixParser().parse(options, args);
        if (!mandatoryFieldsPresent()) {
            throw new UserInterfaceException("Missing mandatory arguments (" + Arrays.toString(MANDATORY_CLI_OPTIONS) + ")");
        }
        return new TaskVariables(getDestinationFolder(), getConnectorConfiguration(), getAction(), getFilesToUpload());
    }

    private List<String> getFilesToUpload() {
        String[] files = getLine().getOptionValues("fu");
        if (files == null) {
            return Collections.emptyList();
        } else {
            return Arrays.asList(files);
        }
    }


    private TaskVariables.Action getAction() {
        if (getLine().getOptionValue("fu") != null) {
            return TaskVariables.Action.UPLOAD_FILE;
        } else if (getLine().hasOption("rd")) {
            return TaskVariables.Action.DOWNLOAD_BY_FILE;
        } else {
            // default
            return TaskVariables.Action.DOWNLOAD_ALL;
        }

    }

    String getDestinationFolder() {
        return getLine().getOptionValue("d");
    }

    SnowConnectorConfiguration getConnectorConfiguration() throws UserInterfaceException {
        return buildProxyConfiguration(SnowConnectorConfiguration.builder())
                .serviceNowDomainName(getLine().getOptionValue("i"))
                .username(getLine().getOptionValue("u"))
                .password(getPassword())
                .build();

    }

    String getPassword() {
        String p = getLine().getOptionValue("p");
        if(p == null || p.isEmpty()) {
            return String.copyValueOf(System.console().readPassword("Password:"));
        } else {
            return p;
        }
    }


    SnowConnectorConfiguration.SnowConnectorConfigurationBuilder buildProxyConfiguration(SnowConnectorConfiguration.SnowConnectorConfigurationBuilder builder) throws UserInterfaceException {
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

    boolean mandatoryFieldsPresent() {
        return Arrays.stream(MANDATORY_CLI_OPTIONS).allMatch(getLine()::hasOption);
    }

    void setupCommandLineOptions() {
        options.addOption("x", "proxy", true, "Use proxy, e.g. 10.0.0.1:3128");
        options.addOption("u", "user", true, "Use this user to connect to ServiceNow");
        options.addOption("p", "password", true, "Use this password to connect to ServiceNow");
        options.addOption("d", "dest", true, "Where to store scripts scripts");
        options.addOption("i", "instance", true, "Instance, e.g. demo019.service-now.com");
        Option fileToUpload = new Option("fu", "fileToUpload", true, "File to upload, e.g. src/hello.js");
        fileToUpload.setArgs(Option.UNLIMITED_VALUES);
        options.addOption(fileToUpload);
        options.addOption("rd", "redownload", false, "Re-download files specified in snow-files.txt");
    }
}