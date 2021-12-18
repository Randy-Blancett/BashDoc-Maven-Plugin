package org.darkowl.bash_doc.output.markdown;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.maven.plugin.logging.SystemStreamLog;
import org.darkowl.bash_doc.TestUtils;
import org.darkowl.bash_doc.builders.FileDataBuilder;
import org.darkowl.bash_doc.model.CommonCommentData;
import org.darkowl.bash_doc.model.FileData;
import org.darkowl.bash_doc.model.Library;
import org.junit.jupiter.api.Test;

class OutputMarkdownTest extends OutputMarkdown {

    @Test
    void test() throws IOException {
        final Library lib = new Library();
        final FileData data = FileDataBuilder.create(Paths.get("target/bash/Test1.sh"));
        lib.getFiles().add(data);
        final Path outputDirectory = Paths.get("target/testOutput/doc");
        Files.createDirectories(outputDirectory);
        final OutputMarkdown output = new OutputMarkdown();
        output.process(new SystemStreamLog(), outputDirectory, lib);
        assertTrue(
                Files.exists(outputDirectory.resolve("markdown").resolve("Test1.md")),
                outputDirectory.resolve("markdown/Test1.sh").toAbsolutePath().toString());
    }

    @Test
    void testCreateCommentBlock() {
        for (int i = 0; i < 6; i++) {
            assertEquals("", createCommentBlock(i, null));
            assertEquals("", createCommentBlock(i, " "));
            TestUtils.testPerLine(createCommentBlock(i, "Hello 1"), "Hello 1");
            TestUtils.testPerLine(createCommentBlock(i, "Hello 1\n    Hello 2"), "Hello 1<br/>", "Hello 2");
            TestUtils.testPerLine(createCommentBlock(i, "Hello 1\n\n    Hello 2\n\n"), "Hello 1<br/>", "Hello 2");
        }
    }

    @Test
    void testCreateExitCodeOutput() {
        for (int i = 0; i < 6; i++) {
            assertEquals("", createExitCodeOutput(i, null, null));
            assertEquals("** 2** - <br/>\n", createExitCodeOutput(i, 2, null));
            assertEquals("Description<br/>\n", createExitCodeOutput(i, null, "Description"));
            assertEquals("** 2** - Description<br/>\n", createExitCodeOutput(i, 2, "Description"));
        }
    }

    @Test
    void testCreateHeaderData() {
        String expectedHead = "";
        for (int i = 0; i < 6; i++) {
            assertEquals("", OutputMarkdown.createHeaderData(i, null, null));
            assertEquals("", OutputMarkdown.createHeaderData(i, " ", null));
            assertEquals("", OutputMarkdown.createHeaderData(i, " ", " "));
            expectedHead += "#";
            assertEquals(expectedHead + " Test1\n", OutputMarkdown.createHeaderData(i, "Test1", " "));
            assertEquals(
                    expectedHead + "  <span style=\"float:right;\"> Right </span>\n",
                    OutputMarkdown.createHeaderData(i, " ", "Right"));
            assertEquals(
                    expectedHead + " Test1<span style=\"float:right;\"> Right </span>\n",
                    OutputMarkdown.createHeaderData(i, "Test1", "Right"));

        }
    }

    @Test
    void testCreatePropertyOutput() {
        for (int i = 0; i < 6; i++) {
            assertEquals("", createPropertyOutput(i, null, null));
            assertEquals("", createPropertyOutput(i, null, "Value"));
            assertEquals("", createPropertyOutput(i, " ", "Value"));
            assertEquals("", createPropertyOutput(i, "Key", null));
            assertEquals("", createPropertyOutput(i, "Key", " "));
            assertEquals("* **Key**: Value\n", createPropertyOutput(i, "Key", "Value"));

        }
    }

    @Test
    void testOutputLine() {
        for (int i = 0; i < 6; i++) {
            final StringBuilder sb = new StringBuilder();
            outputLine(sb, i, null, " ", " ");
            assertEquals("", sb.toString());
            outputLine(sb, i, "Hello  ", " World ");
            assertEquals("*  Hello    World \n", sb.toString());
        }
    }

    @Test
    void testProcess_CommentData() {
        for (int i = 0; i < 6; i++) {
            StringBuilder sb = new StringBuilder();
            process(sb, i, (CommonCommentData) null);
            assertEquals("", sb.toString());

            final CommonCommentData data = new CommonCommentData();

            process(sb, i, data);
            assertEquals("", sb.toString());

            data.setComment("Test Comment");
            process(sb, i, data);
            assertEquals("Test Comment", sb.toString().trim());

            sb = new StringBuilder();
            data.setAuthor("Author1");
            process(sb, i, data);
            TestUtils.testPerLine(sb.toString(), "Test Comment", "", "* **Author**: Author1");

            sb = new StringBuilder();
            data.setAuthorEmail("Author@email.com");
            process(sb, i, data);
            TestUtils.testPerLine(
                    sb.toString(),
                    "Test Comment",
                    "",
                    "* **Author**: Author1",
                    "* **Author Email**: Author@email.com");
        }
    }
}
