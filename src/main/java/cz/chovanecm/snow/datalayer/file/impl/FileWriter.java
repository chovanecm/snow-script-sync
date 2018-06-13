package cz.chovanecm.snow.datalayer.file.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;

public class FileWriter {

    public void writeFile(File file) throws IOException {
        createParentDirectories(file.getFilePath());
        Files.write(file.getFilePath(), file.getByteContent());
        Files.setLastModifiedTime(file.getFilePath(), FileTime.from(file.getLastModified().toInstant()));
    }

    private void createParentDirectories(Path file) throws IOException {
        Path parent = file.getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }
    }
}
