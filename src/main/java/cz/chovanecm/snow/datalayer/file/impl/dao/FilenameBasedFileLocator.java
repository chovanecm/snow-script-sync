package cz.chovanecm.snow.datalayer.file.impl.dao;

import lombok.Getter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FilenameBasedFileLocator implements FileLocator, CategoryIdentifier {
    @Getter
    private Path root;

    public FilenameBasedFileLocator(Path root) {
        this.root = root;
    }

    @Override
    public String getCategoryById(String id) throws IOException {
        Path path = getFileById(id);
        Path relativePath = getRoot().relativize(path);
        return relativePath.getName(0).toString();
    }


    @Override
    public Path getFileById(String id) throws IOException {
        return Files.find(getRoot(), 255, (path, attributes) -> !path.toFile().isDirectory() && path.toString().endsWith(id + ".js"))
                .findFirst().orElseThrow(() -> new FileNotFoundException("No file found for file sys id " + id + " under " + getRoot()));
    }
}