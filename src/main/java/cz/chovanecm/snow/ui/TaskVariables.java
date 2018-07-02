package cz.chovanecm.snow.ui;

import cz.chovanecm.snow.SnowConnectorConfiguration;
import lombok.Value;

/**
 * Task Variables define necessary information for performing a synchronisation task.
 */
@Value
public class TaskVariables {
    private String destinationFolder;
    private SnowConnectorConfiguration connectorConfiguration;
    private Action action;

    public enum Action {
        DOWNLOAD_ALL,
        DOWNLOAD_BY_FILE
    }
}
