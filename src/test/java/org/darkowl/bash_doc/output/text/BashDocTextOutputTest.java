package org.darkowl.bash_doc.output.text;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.maven.plugin.logging.SystemStreamLog;
import org.darkowl.bash_doc.TestUtils;
import org.darkowl.bash_doc.builders.FileDataBuilder;
import org.darkowl.bash_doc.model.CommonCommentData;
import org.darkowl.bash_doc.model.FileData;
import org.darkowl.bash_doc.model.Library;
import org.darkowl.bash_doc.model.VariableData;
import org.junit.jupiter.api.Test;

class BashDocTextOutputTest extends BashDocTextOutput {

    BashDocTextOutput obj = new BashDocTextOutput();

    @Test
    void test() throws IOException {
        final Library lib = new Library();
        final FileData data = FileDataBuilder.create(Paths.get("target/bash/Test1.sh"));
        lib.getFiles().add(data);
        final Path outputDirectory = Paths.get("target/testOutput/doc");
        Files.createDirectories(outputDirectory);
        final BashDocTextOutput output = new BashDocTextOutput();
        output.process(new SystemStreamLog(), outputDirectory, lib);
        assertTrue(
                Files.exists(outputDirectory.resolve("text").resolve("Test1.txt")),
                outputDirectory.resolve("text/Test1.sh").toAbsolutePath().toString());
    }

    @Test
    void testCreateCommentBlock() {
        for (int i = 0; i < 6; i++) {
            assertEquals("", BashDocTextOutput.createCommentBlock(i, null));
            assertEquals("", BashDocTextOutput.createCommentBlock(i, ""));
            final String data = BashDocTextOutput.createCommentBlock(i, "Hello World");
            assertTrue(data.endsWith("Hello World\n"));
            assertEquals(i * 4 + 12, data.length());
        }
        assertEquals("Hi\nall\n", BashDocTextOutput.createCommentBlock(0, "Hi\nall\n "));
    }

    @Test
    void testCreateExitCodeOutput() {
        for (int i = 0; i < 6; i++) {
            assertNull(obj.createExitCodeOutput(i, null, null));

            String data = obj.createExitCodeOutput(i, 1, null);
            assertEquals("1-", data.trim());
            assertEquals(i * 4 + 3, data.length());

            data = obj.createExitCodeOutput(i, null, "Value");
            assertEquals("Value", data.trim());
            assertEquals(i * 4 + 6, data.length());

            data = obj.createExitCodeOutput(i, 1, "Value");
            assertEquals("1- Value", data.trim());
            assertEquals(i * 4 + 9, data.length());

        }
    }

    @Test
    void testCreateParameterOutput() {
        for (int i = 0; i < 6; i++) {
            assertNull(obj.createParameterOutput(i, null, null, null));
            String data = obj.createParameterOutput(i, 1, null, null);
            assertEquals("01 -", data.trim());
            assertEquals(i * 4 + 5, data.length());

            data = obj.createParameterOutput(i, null, "Test", null);
            assertEquals("Test", data.trim());
            assertEquals(i * 4 + 16, data.length());

            data = obj.createParameterOutput(i, null, null, "Some Test Data");
            assertEquals("Some Test Data", data.trim());
            assertEquals(i * 4 + 15, data.length());

            data = obj.createParameterOutput(i, 1, "Test", null);
            assertEquals("01 - Test", data.trim());
            assertEquals(i * 4 + 21, data.length());

            data = obj.createParameterOutput(i, 1, "Test", "Some Test Data.");
            assertEquals("01 - Test            Some Test Data.", data.trim());
            assertEquals(i * 4 + 37, data.length());
        }
    }

    @Test
    void testCreatePropertyOutput() {
        for (int i = 0; i < 6; i++) {
            assertEquals("", BashDocTextOutput.createPropertyOutput(i, null, null));
            assertEquals("", BashDocTextOutput.createPropertyOutput(i, "Key", null));
            assertEquals("", BashDocTextOutput.createPropertyOutput(i, null, "Value"));
            final String data = BashDocTextOutput.createPropertyOutput(i, "Key", "Value");
            assertEquals("- Key: Value", data.trim());
            assertEquals(i * 4 + 14, data.length());

        }
    }

    @Test
    void testOutputLine() {
        final StringBuilder sb = new StringBuilder();
        BashDocTextOutput.outputLine(
                sb,
                0,
                "The ",
                "quick ",
                "brown ",
                "fox ",
                "jumps over the lazy dog.",
                "The quick brown fox jumps over the lazy dog. ",
                "the quick brown fox jumps over the lazy dog.",
                "there are some other things to think about when doing this ");
        final String[] testArray = sb.toString().split("\n");
        for (final String testItem : testArray)
            assertTrue(testItem.length() <= BashDocTextOutput.LINE_WIDTH);
        assertEquals("The quick brown fox jumps over the lazy dog. The quick brown fox jumps over the", testArray[0]);
        assertEquals("    lazy dog. the quick brown fox jumps over the lazy dog. there are some other", testArray[1]);
        assertEquals("    things to think about when doing this", testArray[2]);
    }

    @Test
    void testOutputLine_bug1() {
        final StringBuilder sb = new StringBuilder();
        BashDocTextOutput.outputLine(
                sb,
                0,
                null,
                "",
                null,
                "fox ",
                "jumps over the lazy dog.",
                "The quick brown fox jumps over the lazy dog. ",
                "the quick brown fox jumps over the lazy dog.",
                "there are some other things to think about when doing this ");
        final String[] testArray = sb.toString().split("\n");
        for (final String testItem : testArray)
            assertTrue(testItem.length() <= BashDocTextOutput.LINE_WIDTH);
        assertEquals("fox jumps over the lazy dog. The quick brown fox jumps over the lazy dog. the", testArray[0]);
    }

    @Test
    void testProcess_CommentData() {
        for (int i = 0; i < 6; i++) {
            StringBuilder sb = new StringBuilder();
            obj.process(sb, i, (CommonCommentData) null);
            assertEquals("", sb.toString());

            final CommonCommentData data = new CommonCommentData();

            obj.process(sb, i, data);
            assertEquals("", sb.toString());

            data.setComment("Test Comment");
            obj.process(sb, i, data);
            assertEquals("Test Comment", sb.toString().trim());

            sb = new StringBuilder();
            data.setAuthor("Author1");
            obj.process(sb, i, data);
            TestUtils.testPerLine(sb.toString(), "Test Comment", "- Author: Author1");

            sb = new StringBuilder();
            data.setAuthorEmail("Author@email.com");
            obj.process(sb, i, data);
            TestUtils.testPerLine(
                    sb.toString(),
                    "Test Comment",
                    "- Author: Author1",
                    "- Author Email: Author@email.com");
        }
    }

    @Test
    /**
     * This dosen't test a whole lot other than make sure there are no run time
     * errors
     *
     * @throws IOException
     */
    void testProcess_Special() throws IOException {
        final Path outputDirectory = Paths.get("target/testOutput/doc");
        final BashDocTextOutput output = new BashDocTextOutput();
        output.process(new SystemStreamLog(), outputDirectory, null);
        final Library lib = new Library();
        lib.setCreated(new Date());
        lib.getFiles().add(null);
        output.process(new SystemStreamLog(), outputDirectory, lib);
    }

    @Test
    void testProcess_VariableData_Special() {
        final StringBuilder sb = new StringBuilder();
        final VariableData data = null;
        obj.process(sb, 0, data);
        assertEquals("", sb.toString());
    }

    @Test
    void testProcessExamples() {
        for (int i = 0; i < 6; i++) {
            final StringBuilder sb = new StringBuilder();
            List<String> data = null;
            obj.processExamples(sb, i, data);
            assertEquals("", sb.toString());

            data = new ArrayList<>();
            obj.processExamples(sb, i, data);
            assertEquals("", sb.toString());

            data.add(null);
            obj.processExamples(sb, i, data);
            assertEquals("", sb.toString());

            data.add(" ");
            obj.processExamples(sb, i, data);
            assertEquals("", sb.toString());

            data.add("Some Example");
            obj.processExamples(sb, i, data);
            TestUtils.testPerLine(sb.toString().replace("*", "").trim(), "Examples", "", "Some Example");
        }
    }

}
