package org.darkowl.bash_doc.output;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.darkowl.bash_doc.model.CommonCommentData;
import org.darkowl.bash_doc.model.VariableData;
import org.junit.jupiter.api.Test;

class OutputFormatterTest extends OutputFormatter {

    protected OutputFormatterTest() {
        super("test", "test");
    }

    @Test
    void testAddHeader() {
        for (int i = 0; i < 6; i++) {
            final StringBuilder sb = new StringBuilder();
            addHeader(sb, i, "Test1", "Test2");
            assertEquals("", sb.toString());
        }
    }

    @Test
    void testCreateExitCodeOutput() {
        for (int i = 0; i < 6; i++)
            assertEquals("", createExitCodeOutput(i, 1, "Hello"));
    }

    @Test
    void testCreateParameterOutput() {
        for (int i = 0; i < 6; i++)
            assertEquals("", createParameterOutput(i, 1, "Var", "Hello"));
    }

    @Test
    void testOutputLine() {
        for (int i = 0; i < 6; i++) {
            final StringBuilder sb = new StringBuilder();
            outputLine(sb, i, "Test1", "Test2");
            assertEquals("", sb.toString());
        }
    }

    @Test
    void testprocess_CommonCommentData() {
        for (int i = 0; i < 6; i++) {
            final StringBuilder sb = new StringBuilder();
            final CommonCommentData data = new CommonCommentData();
            data.setComment("Hello");
            process(sb, i, data);
            assertEquals("", sb.toString());
        }
    }

    @Test
    void testprocess_VariableData() {
        for (int i = 0; i < 6; i++) {
            final StringBuilder sb = new StringBuilder();
            final VariableData data = new VariableData();
            data.setComment("Hello");
            process(sb, i, data);
            assertEquals("", sb.toString());
        }
    }

    @Test
    void testProcessReturn() {
        for (int i = 0; i < 6; i++) {
            final StringBuilder sb = new StringBuilder();
            processReturn(sb, i, "Hello");
            assertEquals("", sb.toString());
        }
    }

    @Test
    void testWriteFileData() {
        writeFileData("hello", "Other".getBytes());
    }

}
