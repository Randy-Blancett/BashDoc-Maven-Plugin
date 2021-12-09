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
import org.darkowl.bash_doc.builders.FileDataBuilder;
import org.darkowl.bash_doc.model.CommonCommentData;
import org.darkowl.bash_doc.model.FileData;
import org.darkowl.bash_doc.model.Library;
import org.darkowl.bash_doc.model.MethodData;
import org.darkowl.bash_doc.model.ScopeType;
import org.darkowl.bash_doc.model.VariableData;
import org.darkowl.bash_doc.model.VariableType;
import org.darkowl.bash_doc.model.VersionHistoryData;
import org.junit.jupiter.api.Test;

class BashDocTextOutputTest {

    @Test
    void test() throws IOException {
        final Library lib = new Library();
        final FileData data = FileDataBuilder.create(Paths.get("target/bash/Test1.sh"));
        lib.getFiles().add(data);
        final Path outputDirectory = Paths.get("target/testOutput/doc");
        Files.createDirectories(outputDirectory);
        BashDocTextOutput.output(new SystemStreamLog(), outputDirectory, lib);
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
            assertNull(BashDocTextOutput.createExitCodeOutput(i, null, null));

            String data = BashDocTextOutput.createExitCodeOutput(i, 1, null);
            assertEquals("1-", data.trim());
            assertEquals(i * 4 + 3, data.length());

            data = BashDocTextOutput.createExitCodeOutput(i, null, "Value");
            assertEquals("Value", data.trim());
            assertEquals(i * 4 + 6, data.length());

            data = BashDocTextOutput.createExitCodeOutput(i, 1, "Value");
            assertEquals("1- Value", data.trim());
            assertEquals(i * 4 + 9, data.length());

        }
    }

    @Test
    void testCreateParameterOutput() {
        for (int i = 0; i < 6; i++) {
            assertNull(BashDocTextOutput.createParameterOutput(i, null, null, null));
            String data = BashDocTextOutput.createParameterOutput(i, 1, null, null);
            assertEquals("01 -", data.trim());
            assertEquals(i * 4 + 5, data.length());

            data = BashDocTextOutput.createParameterOutput(i, null, "Test", null);
            assertEquals("Test", data.trim());
            assertEquals(i * 4 + 16, data.length());

            data = BashDocTextOutput.createParameterOutput(i, null, null, "Some Test Data");
            assertEquals("Some Test Data", data.trim());
            assertEquals(i * 4 + 15, data.length());

            data = BashDocTextOutput.createParameterOutput(i, 1, "Test", null);
            assertEquals("01 - Test", data.trim());
            assertEquals(i * 4 + 21, data.length());

            data = BashDocTextOutput.createParameterOutput(i, 1, "Test", "Some Test Data.");
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

    void testPerLine(final String raw, final String... expected) {
        final String[] data = raw.split("\n");
        assertEquals(expected.length, data.length, "Full Data: " + raw);
        for (int i = 0; i < data.length; i++)
            assertEquals(expected[i], data[i].trim(), "Full Data: " + raw);
    }

    @Test
    void testProcess_CommentData() {
        for (int i = 0; i < 6; i++) {
            StringBuilder sb = new StringBuilder();
            BashDocTextOutput.process(sb, i, (CommonCommentData) null);
            assertEquals("", sb.toString());

            final CommonCommentData data = new CommonCommentData();

            BashDocTextOutput.process(sb, i, data);
            assertEquals("", sb.toString());

            data.setComment("Test Comment");
            BashDocTextOutput.process(sb, i, data);
            assertEquals("Test Comment", sb.toString().trim());

            sb = new StringBuilder();
            data.setAuthor("Author1");
            BashDocTextOutput.process(sb, i, data);
            testPerLine(sb.toString(), "Test Comment", "- Author: Author1");

            sb = new StringBuilder();
            data.setAuthorEmail("Author@email.com");
            BashDocTextOutput.process(sb, i, data);
            testPerLine(sb.toString(), "Test Comment", "- Author: Author1", "- Author Email: Author@email.com");
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
        final BashDocTextOutput output = new BashDocTextOutput(new SystemStreamLog(), outputDirectory);
        output.process(null);
        final Library lib = new Library();
        lib.setCreated(new Date());
        lib.getFiles().add(null);
        output.process(lib);
    }

    @Test
    void testProcess_VersionHistory() {
        for (int i = 0; i < 6; i++) {
            StringBuilder sb = new StringBuilder();
            List<VersionHistoryData> data = null;
            BashDocTextOutput.process(sb, i, data);
            assertEquals("", sb.toString());

            data = new ArrayList<>();

            BashDocTextOutput.process(sb, i, data);
            assertEquals("", sb.toString());

            data.add(null);
            BashDocTextOutput.process(sb, i, data);
            assertEquals("", sb.toString());

            final VersionHistoryData item = new VersionHistoryData();
            data.add(item);
            BashDocTextOutput.process(sb, i, data);
            assertEquals("", sb.toString());

            item.setVersion(" ");
            BashDocTextOutput.process(sb, i, data);
            assertEquals("", sb.toString());

            item.setVersion("1.0.0");
            BashDocTextOutput.process(sb, i, data);
            testPerLine(sb.toString().replace("*", ""), "", "Version History", "", "", "", "1.0.0", "");

            sb = new StringBuilder();
            item.setAuthor("Author1");
            item.setAuthorEmail("Author1@email.com");
            BashDocTextOutput.process(sb, i, data);
            testPerLine(
                    sb.toString().replace("*", ""),
                    "",
                    "Version History",
                    "",
                    "",
                    "",
                    "1.0.0",
                    "",
                    "- Author: Author1",
                    "- Author Email: Author1@email.com");

            sb = new StringBuilder();
            item.setComment("Some Comment\n Some Other Comment");
            BashDocTextOutput.process(sb, i, data);
            testPerLine(
                    sb.toString().replace("*", ""),
                    "",
                    "Version History",
                    "",
                    "",
                    "",
                    "1.0.0",
                    "",
                    "Some Comment",
                    "Some Other Comment",
                    "- Author: Author1",
                    "- Author Email: Author1@email.com");

            sb = new StringBuilder();
            item.setRelease("Jan 2021");
            BashDocTextOutput.process(sb, 0, data);
            testPerLine(
                    sb.toString().replace("*", ""),
                    "",
                    "Version History",
                    "",
                    "",
                    "",
                    "1.0.0                                                           Jan 2021",
                    "",
                    "Some Comment",
                    "Some Other Comment",
                    "- Author: Author1",
                    "- Author Email: Author1@email.com");

        }
    }

    @Test
    void testProcessVariables() {
        for (int i = 0; i < 6; i++) {
            StringBuilder sb = new StringBuilder();
            List<VariableData> data = null;
            BashDocTextOutput.processVariables(sb, i, data);
            assertEquals("", sb.toString());

            data = new ArrayList<>();
            BashDocTextOutput.processVariables(sb, i, data);
            assertEquals("", sb.toString());

            data.add(null);
            BashDocTextOutput.processVariables(sb, i, data);
            assertEquals("", sb.toString());

            VariableData item = new VariableData();
            data.add(item);
            BashDocTextOutput.processVariables(sb, i, data);
            assertEquals("", sb.toString());

            item.setName(" ");
            BashDocTextOutput.processVariables(sb, i, data);
            assertEquals("", sb.toString());

            item.setName("Var1");
            BashDocTextOutput.processVariables(sb, i, data);
            testPerLine(sb.toString().replace("*", "").trim(), "Variables", "", "", "", "Var1");

            sb = new StringBuilder();
            item = new VariableData();
            data.add(item);
            item.setName("Var2");
            item.setAuthor("Author 1");
            item.setAuthorEmail("Author1@email.com");
            item.setComment("This is the Second Variable.");
            item.setDefault("Data");
            item.setType(VariableType.STRING);
            BashDocTextOutput.processVariables(sb, i, data);
            testPerLine(
                    sb.toString().replace("*", "").trim(),
                    "Variables",
                    "",
                    "",
                    "",
                    "Var1",
                    "",
                    "",
                    "",
                    "Var2",
                    "",
                    "This is the Second Variable.",
                    "- Author: Author 1",
                    "- Author Email: Author1@email.com",
                    "- Default Value: Data");

            sb = new StringBuilder();
            item.setScope(ScopeType.PROTECTED);
            BashDocTextOutput.processVariables(sb, 0, data);
            testPerLine(
                    sb.toString().replace("*", "").trim(),
                    "Variables",
                    "",
                    "",
                    "",
                    "Var2                                                           PROTECTED",
                    "",
                    "This is the Second Variable.",
                    "- Author: Author 1",
                    "- Author Email: Author1@email.com",
                    "- Default Value: Data",
                    "",
                    "",
                    "Var1");
        }
    }

    @Test
    void testProcess_VariableData_Special() {
        StringBuilder sb = new StringBuilder();
        VariableData data = null;
        BashDocTextOutput.process(sb, 0, data);
        assertEquals("", sb.toString());
    }

    @Test
    void testProcessExamples() {
        for (int i = 0; i < 6; i++) {
            StringBuilder sb = new StringBuilder();
            List<String> data = null;
            BashDocTextOutput.processExamples(sb, i, data);
            assertEquals("", sb.toString());

            data = new ArrayList<>();
            BashDocTextOutput.processExamples(sb, i, data);
            assertEquals("", sb.toString());

            data.add(null);
            BashDocTextOutput.processExamples(sb, i, data);
            assertEquals("", sb.toString());

            data.add(" ");
            BashDocTextOutput.processExamples(sb, i, data);
            assertEquals("", sb.toString());

            data.add("Some Example");
            BashDocTextOutput.processExamples(sb, i, data);
            testPerLine(sb.toString().replace("*", "").trim(), "Examples", "", "Some Example");
        }
    }

    @Test
    void testProcessMethods() {
        for (int i = 0; i < 6; i++) {
            StringBuilder sb = new StringBuilder();
            List<MethodData> data = null;
            BashDocTextOutput.processMethods(sb, i, data);
            assertEquals("", sb.toString());

            data = new ArrayList<>();

            BashDocTextOutput.processMethods(sb, i, data);
            assertEquals("", sb.toString());

            data.add(null);
            BashDocTextOutput.processMethods(sb, i, data);
            testPerLine(sb.toString().replace("*", "").trim(), "Methods");

            sb = new StringBuilder();
            final MethodData item = new MethodData();
            data.add(item);
            BashDocTextOutput.processMethods(sb, i, data);
            testPerLine(sb.toString().replace("*", "").trim(), "Methods");

            sb = new StringBuilder();
            item.setName(" ");
            BashDocTextOutput.processMethods(sb, i, data);
            testPerLine(sb.toString().replace("*", "").trim(), "Methods");

            sb = new StringBuilder();
            item.setName("Method 1");
            item.setAuthor("Author1");
            item.setAuthorEmail("Author1@email.com");
            BashDocTextOutput.processMethods(sb, i, data);
            testPerLine(
                    sb.toString().replace("*", ""),
                    "",
                    "Methods",
                    "",
                    "",
                    "",
                    "Method 1",
                    "",
                    "- Author: Author1",
                    "- Author Email: Author1@email.com");

            sb = new StringBuilder();
            item.setComment("Comment1\nComment2");
            item.setReturn("Return Some Value...");
            BashDocTextOutput.processMethods(sb, i, data);
            testPerLine(
                    sb.toString().replace("*", ""),
                    "",
                    "Methods",
                    "",
                    "",
                    "",
                    "Method 1",
                    "",
                    "Comment1",
                    "Comment2",
                    "- Author: Author1",
                    "- Author Email: Author1@email.com",
                    "",
                    "",
                    "Return",
                    "",
                    "Return Some Value...");

            sb = new StringBuilder();
            item.setScope(ScopeType.PUBLIC);
            BashDocTextOutput.processMethods(sb, 0, data);
            testPerLine(
                    sb.toString().replace("*", ""),
                    "",
                    "Methods",
                    "",
                    "",
                    "",
                    "Method 1                                                          PUBLIC",
                    "",
                    "Comment1",
                    "Comment2",
                    "- Author: Author1",
                    "- Author Email: Author1@email.com",
                    "",
                    "",
                    "Return",
                    "",
                    "Return Some Value...");

        }
    }
}
