package space.mira;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {

    //проверка проходила на конкретном пути
    final String PATH = "C:/Users/1/Desktop/dir1";
    final String EXPECTED_TEXT = "its java!\nHello\nEntire World!";

    FilesScanner filesScanner;
    List<TextFile> textFiles;

    @BeforeEach
    void setUp() {
        filesScanner = new FilesScanner();
        textFiles = new ArrayList<>();
    }

    @Test
    public void mainTest() {
        filesScanner.findAndCollectTextFiles(PATH, textFiles);
        String actual = "";
        for (TextFile file : textFiles) {
            actual += file.getText() + "\n";
        }
        assertEquals(EXPECTED_TEXT, actual.trim());
    }

    @AfterEach
    void tearDown() {
    }
}