package operations;

import gui_forms.Archive;
import gui_forms.TextForm;
import org.junit.Test;

import java.io.IOException;
import java.sql.SQLException;

import static org.junit.Assert.*;

public class CheckTextFileTest extends Archive {
    TextForm textForm = new TextForm();

    public CheckTextFileTest() throws SQLException, IOException {
    }

    @Test
    public void isIndicatorAll() throws Exception {
        TextReader.methodToolSearchPositionsInTheText(textForm.showExampleTextFromResource("/text/act.txt"));
        assertEquals("message", true, CheckTextFile.isIndicatorAll(textForm.showExampleTextFromResource("/text/act.txt")));
    }

    @Test
    public void methodCheckCountWords() throws IOException {
        assertEquals("message", true, CheckTextFile.methodCheckCountWords(textForm.showExampleTextFromResource("/text/act.txt")));
    }

    @Test
    public void methodCheckCountSentences() {
    }

    @Test
    public void methodCheckParagraphsInText() {
    }

    @Test
    public void methodCheckCaptions() throws IOException {
        assertEquals("message", true, CheckTextFile.methodCheckCaptions(textForm.showExampleTextFromResource("/text/act.txt")));
    }

    @Test
    public void methodCheckPresenceOfSentenceInTheText() throws IOException {
        assertEquals("message", true,
                CheckTextFile.methodCheckPresenceOfSentenceInTheText(textForm.showExampleTextFromResource("/text/act.txt")));
    }


    @Test
    public void methodCheckModelRocket() throws IOException {
        assertEquals("message", true,
                CheckTextFile.methodCheckModelRocket(textForm.showExampleTextFromResource("/text/act.txt")));
    }

    @Test
    public void methodFindQuantityPersons() throws Exception {
        TextReader.methodToolSearchPositionsInTheText(textForm.showExampleTextFromResource("/text/act.txt"));
        assertEquals("message", true,
                CheckTextFile.methodFindQuantityPersons(textForm.showExampleTextFromResource("/text/act.txt")));
    }

    @Test
    public void methodCheckCityBaseInTheText() throws IOException {
        assertEquals("message", true,
                CheckTextFile.methodCheckCityBaseInTheText(textForm.showExampleTextFromResource("/text/act.txt")));
    }

    @Test
    public void checkPositionAndGradeOfCommanderChief() throws IOException {
        assertEquals("message", true,
                CheckTextFile.checkPositionAndGradeOfCommanderChief(textForm.showExampleTextFromResource("/text/act.txt")));
    }

}