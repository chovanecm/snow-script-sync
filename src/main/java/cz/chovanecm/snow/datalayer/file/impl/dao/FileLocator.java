package cz.chovanecm.snow.datalayer.file.impl.dao;

import java.io.IOException;
import java.nio.file.Path;

public interface FileLocator {
    Path getFileById(String id) throws IOException;
}
