package org.darkowl.bash_doc.output.text;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.maven.plugin.logging.SystemStreamLog;
import org.darkowl.bash_doc.builders.FileDataBuilder;
import org.darkowl.bash_doc.model.FileData;
import org.darkowl.bash_doc.model.Library;
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

}
