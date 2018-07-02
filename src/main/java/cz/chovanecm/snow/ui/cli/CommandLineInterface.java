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
import cz.chovanecm.snow.ui.TaskVariables;
import cz.chovanecm.snow.ui.UserInterfaceException;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.ParseException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CommandLineInterface {
    private final CommandLineParser commandLineParser;
    private final TaskVariables result;
    private Map<TaskVariables.Action, Runnable> availableActions = new HashMap<>();


    public CommandLineInterface(String[] args) throws UserInterfaceException {
        commandLineParser = new CommandLineParser();
        try {
            result = commandLineParser.parse(args);
            SnowConnectorConfiguration configuration = result.getConnectorConfiguration();
            SnowScriptSynchronizer synchronizer = new SnowScriptSynchronizer(configuration, result.getDestinationFolder());

            availableActions.put(TaskVariables.Action.DOWNLOAD_ALL, synchronizer::downloadAll);
            availableActions.put(TaskVariables.Action.DOWNLOAD_BY_FILE, synchronizer::downloadByFile);
        } catch (ParseException | UserInterfaceException e) {
            System.err.println(String.format("Error when parsing arguments %s. Cause: %s", Arrays.toString(args), e.getMessage()));
            printHelp();
            throw new UserInterfaceException(e);
        }
    }

    public static void main(String[] args) {
        try {
            CommandLineInterface userInterface = null;
            userInterface = new CommandLineInterface(args);
            userInterface.runTask();
        } catch (UserInterfaceException e) {
            // just exit
        }
    }

    public void runTask() {
        availableActions.get(result.getAction()).run();
    }

    public void printHelp() {
        new HelpFormatter().printHelp("_", commandLineParser.getOptions());
    }


}
