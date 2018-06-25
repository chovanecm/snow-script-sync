package cz.chovanecm.snow.datalayer.file.impl.dao;

import cz.chovanecm.snow.records.SnowScript;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class FileSystemDaoTest {
    public static final String[] SCRIPT_CONTENT = {"First line", "Second line"};

    @Test
    public void testThatIUnderstoodReduceCorrectly() {
        List<String> list = Arrays.asList("First line", "Second line", "Third line", "Fourth line");
        String result = list.stream().reduce("", String::concat);
        assertEquals("First lineSecond lineThird lineFourth line", result);
    }

    @Test
    public void testGetFileById_shouldReturnPath_whenFileExists() throws URISyntaxException, IOException {
        Path rootPath = Paths.get(getClass().getResource("/cz.chovanecm.snow.datalayer.file.impl.dao").toURI());
        Path file1 = rootPath.resolve("subdirectory1/My script_SYSIDABC123.js");
        Path file2 = rootPath.resolve("subdirectory2/My script_SYSIDXYZ000.js");
        givenFileExists(file1);
        givenFileExists(file2);
        FileSystemDao dao = new FileSystemDao(rootPath);

        assertEquals(file2, dao.getFileById("SYSIDXYZ000"));
    }

    @Test
    public void testGetFileById_shouldThrowException_whenFileDoesNotExist() throws URISyntaxException {
        Path rootPath = Paths.get(getClass().getResource("/cz.chovanecm.snow.datalayer.file.impl.dao").toURI());
        FileSystemDao dao = new FileSystemDao(rootPath);

        assertThrows(FileNotFoundException.class, () -> dao.getFileById("RANDOMRANDOMRANDOm"));
    }

    @Test
    public void testGetCategoryById_shouldReturnDirectoryNameInWhichTheFileResides() throws URISyntaxException, IOException {
        Path rootPath = Paths.get(getClass().getResource("/cz.chovanecm.snow.datalayer.file.impl.dao").toURI());
        Path file2 = rootPath.resolve("subdirectory2/My script_SYSIDXYZ000.js");
        givenFileExists(file2);
        FileSystemDao dao = new FileSystemDao(rootPath);

        assertEquals("subdirectory2", dao.getCategoryById("SYSIDXYZ000"));
    }


    @Test
    public void testGetById_shouldReturnSnowScriptWithAppropriateContent() throws URISyntaxException, IOException {
        Path rootPath = Paths.get(getClass().getResource("/cz.chovanecm.snow.datalayer.file.impl.dao").toURI());
        Path file2 = rootPath.resolve("subdirectory2/My script_SYSIDXYZ000.js");
        givenFileExists(file2);
        FileSystemDao dao = new FileSystemDao(rootPath);

        // WHEN
        SnowScript script = dao.get("SYSIDXYZ000");

        //THEN
        assertEquals("SYSIDXYZ000", script.getSysId(), "SysID should be set");
        assertEquals(String.join(System.lineSeparator(), SCRIPT_CONTENT), script.getScript(), "Script should match");
    }

    private void givenFileExists(Path fileName) throws IOException {
        Files.write(fileName, Arrays.asList(SCRIPT_CONTENT));
    }
}