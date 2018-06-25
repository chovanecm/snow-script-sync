package cz.chovanecm.snow.datalayer.file.impl.dao;

import cz.chovanecm.snow.records.SnowScript;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class FileSystemDaoTest {
    public static final String[] SCRIPT_CONTENT = {"First line", "Second line"};

    @Test
    public void testThatIUnderstoodReduceCorrectly() {
        List<String> list = Arrays.asList("First line", "Second line", "Third line", "Fourth line");
        String result = list.stream().reduce("", String::concat);
        assertEquals("First lineSecond lineThird lineFourth line", result);
    }


    @Test
    public void testGetById_shouldReturnSnowScriptWithAppropriateContent() throws URISyntaxException, IOException {
        Path rootPath = Paths.get(getClass().getResource("/cz.chovanecm.snow.datalayer.file.impl.dao").toURI());
        Path file2 = rootPath.resolve("subdirectory2/My script_SYSIDXYZ000.js");
        givenFileExists(file2);
        FileSystemDao dao = new FileSystemDao(new FilenameBasedFileLocator(rootPath));

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