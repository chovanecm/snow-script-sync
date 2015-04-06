/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.chovanecm.snow.cli;

import cz.chovanecm.snow.SnowSycriptSynchronizer;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

/**
 *
 * @author Martin
 */
public class CommandLineInterface {

    private static CommandLine line;
    private static Options options = new Options();

    public static void main(String[] args) {
        options.addOption("x", "proxy", true, "Use proxy, e.g. 10.0.0.1:3128");
        options.addOption("u", "user", true, "Use this user to connect to ServiceNow");
        options.addOption("p", "password", true, "Password to ServiceNow");
        options.addOption("d", "dest", true, "Where to download scripts");
        options.addOption("i", "instance", true, "Instance, e.g. demo019.service-now.com");
        try {
            line = new PosixParser().parse(options, args);
            if (!line.hasOption("d") || !line.hasOption("u") || !line.hasOption("p")) {
                System.out.println("Destionation, instance, user and password are mandatory.");
                new HelpFormatter().printHelp("_", options);
                return;
            } else {
                String proxy = null;
                Integer proxyPort = null;
                if (line.hasOption("x")) {
                    String [] proxyString = line.getOptionValue("x").split(":");
                    if (proxyString.length != 2) {
                        System.out.println("Proxy format is host:port.");
                        return;
                    } else {
                        try {
                        proxyPort = Integer.parseInt(proxyString[1]);
                        proxy = proxyString[0];
                        } catch (NumberFormatException ex) {
                            System.out.println("Port number must be a number.");
                            return;
                        }
                    }
                }
                SnowSycriptSynchronizer.run("https://" + line.getOptionValue("i"), line.getOptionValue("u"), line.getOptionValue("p"), proxy, proxyPort, line.getOptionValue("d"));
            }
        } catch (ParseException ex) {
            new HelpFormatter().printHelp("_", options);
        } catch (IOException ex) {
            System.out.println("There was an error during the action. :-/");
            Logger.getLogger(CommandLineInterface.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
