package cz.chovanecm.snow.datalayer.file.impl.dao;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class FilenameBasedFileLocatorTest {

    private FileLocator fileLocator;
    private Path file2;
    private Path file1;

    @BeforeAll
    public void setUp() throws IOException, URISyntaxException {
        Path rootPath = Paths.get(getClass().getResource("/cz.chovanecm.snow.datalayer.file.impl.dao").toURI());
        file1 = rootPath.resolve("subdirectory1/My script_SYSIDABC123.js");
        file2 = rootPath.resolve("subdirectory2/My script_SYSIDXYZ000.js");
        givenFileExists(file1);
        givenFileExists(file2);

        fileLocator = new FilenameBasedFileLocator(rootPath);
    }


    @Test
    public void testGetFileById_shouldReturnPath_whenFileExists() throws IOException {
        assertEquals(file2, fileLocator.getFileById("SYSIDXYZ000"));
    }

    @Test
    public void testGetFileById_shouldThrowException_whenFileDoesNotExist() {
        assertThrows(FileNotFoundException.class, () -> fileLocator.getFileById("RANDOMRANDOMRANDOm"));
    }

    @Test
    public void testGetCategoryById_shouldReturnDirectoryNameInWhichTheFileResides() throws IOException {
        assertEquals("subdirectory2", fileLocator.getCategoryById("SYSIDXYZ000"));
    }

    private void givenFileExists(Path fileName) throws IOException {
        Files.write(fileName, Arrays.asList(""));
    }
}