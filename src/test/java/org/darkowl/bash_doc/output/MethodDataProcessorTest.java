package org.darkowl.bash_doc.output;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.darkowl.bash_doc.TestUtils;
import org.darkowl.bash_doc.model.MethodData;
import org.darkowl.bash_doc.model.ScopeType;
import org.darkowl.bash_doc.output.text.BashDocTextOutput;
import org.junit.jupiter.api.Test;

class MethodDataProcessorTest {

    BashDocTextOutput obj = new BashDocTextOutput();

    @Test
    void testProcess() {
        for (int i = 0; i < 6; i++) {
            StringBuilder sb = new StringBuilder();
            List<MethodData> data = null;
            MethodDataProcessor.process(obj, sb, i, data);
            assertEquals("", sb.toString());

            data = new ArrayList<>();

            MethodDataProcessor.process(obj, sb, i, data);
            assertEquals("", sb.toString());

            data.add(null);
            MethodDataProcessor.process(obj, sb, i, data);
            assertEquals("", sb.toString());

            sb = new StringBuilder();
            final MethodData item = new MethodData();
            data.add(item);
            MethodDataProcessor.process(obj, sb, i, data);
            assertEquals("", sb.toString());

            sb = new StringBuilder();
            item.setName(" ");
            MethodDataProcessor.process(obj, sb, i, data);
            assertEquals("", sb.toString());

            sb = new StringBuilder();
            item.setName("Method 1");
            item.setAuthor("Author1");
            item.setAuthorEmail("Author1@email.com");
            MethodDataProcessor.process(obj, sb, i, data);
            TestUtils.testPerLine(
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
            MethodDataProcessor.process(obj, sb, i, data);
            TestUtils.testPerLine(
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
            MethodDataProcessor.process(obj, sb, 0, data);
            TestUtils.testPerLine(
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
