package org.darkowl.bash_doc.output;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.darkowl.bash_doc.TestUtils;
import org.darkowl.bash_doc.output.text.BashDocTextOutput;
import org.junit.jupiter.api.Test;

class ExampleProcessorTest {
    BashDocTextOutput obj = new BashDocTextOutput();

    @Test
    void testProcessExamples() {
        for (int i = 0; i < 6; i++) {
            final StringBuilder sb = new StringBuilder();
            List<String> data = null;
            ExampleProcessor.process(obj, sb, i, data);
            assertEquals("", sb.toString());

            data = new ArrayList<>();
            ExampleProcessor.process(obj, sb, i, data);
            assertEquals("", sb.toString());

            data.add(null);
            ExampleProcessor.process(obj, sb, i, data);
            assertEquals("", sb.toString());

            data.add(" ");
            ExampleProcessor.process(obj, sb, i, data);
            assertEquals("", sb.toString());

            data.add("Some Example");
            ExampleProcessor.process(obj, sb, i, data);
            TestUtils.testPerLine(sb.toString().replace("*", "").trim(), "Examples", "", "Some Example");
        }
    }
}
