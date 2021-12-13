package org.darkowl.bash_doc.output;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.darkowl.bash_doc.TestUtils;
import org.darkowl.bash_doc.model.ExitCodeData;
import org.darkowl.bash_doc.output.text.BashDocTextOutput;
import org.junit.jupiter.api.Test;

class ExitCodeDataProcessorTest {
    BashDocTextOutput obj = new BashDocTextOutput();

    @Test
    void testExitCode() {
        for (int i = 0; i < 6; i++) {
            StringBuilder sb = new StringBuilder();
            List<ExitCodeData> data = null;
            ExitCodeDataProcessor.process(obj, sb, i, data);
            assertEquals("", sb.toString());

            data = new ArrayList<>();
            ExitCodeDataProcessor.process(obj, sb, i, data);
            assertEquals("", sb.toString());

            data.add(null);
            ExitCodeDataProcessor.process(obj, sb, i, data);
            assertEquals("", sb.toString());

            ExitCodeData item = new ExitCodeData();
            data.add(item);
            ExitCodeDataProcessor.process(obj, sb, i, data);
            assertEquals("", sb.toString());

            item.setCode(1);
            ExitCodeDataProcessor.process(obj, sb, i, data);
            TestUtils.testPerLine(sb.toString().replace("*", "").trim(), "Exit Codes", "", "1-");

            item = new ExitCodeData();
            data = new ArrayList<>();
            sb = new StringBuilder();
            item.setDescription(" ");
            data.add(item);
            ExitCodeDataProcessor.process(obj, sb, i, data);
            assertEquals("", sb.toString());

            item.setDescription("Description 1");
            ExitCodeDataProcessor.process(obj, sb, i, data);
            TestUtils.testPerLine(sb.toString().replace("*", "").trim(), "Exit Codes", "", "Description 1");

            sb = new StringBuilder();
            item.setCode(1);
            item = new ExitCodeData();
            data.add(item);
            item.setCode(2);
            item.setDescription("Description 2");
            ExitCodeDataProcessor.process(obj, sb, i, data);
            TestUtils.testPerLine(
                    sb.toString().replace("*", "").trim(),
                    "Exit Codes",
                    "",
                    "1- Description 1",
                    "2- Description 2");
        }
    }

}
