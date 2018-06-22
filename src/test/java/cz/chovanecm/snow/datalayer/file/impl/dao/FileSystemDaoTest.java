package cz.chovanecm.snow.datalayer.file.impl.dao;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class FileSystemDaoTest {
    @Test
    public void testThatIUnderstoodReduceCorrectly() {
        List<String> list = Arrays.asList("First line", "Second line", "Third line", "Fourth line");
        String result = list.stream().reduce("", String::concat);
        assertEquals("First lineSecond lineThird lineFourth line", result);
    }
}