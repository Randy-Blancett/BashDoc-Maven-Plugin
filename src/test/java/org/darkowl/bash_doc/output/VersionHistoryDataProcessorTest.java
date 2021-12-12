package org.darkowl.bash_doc.output;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.darkowl.bash_doc.TestUtils;
import org.darkowl.bash_doc.model.VersionHistoryData;
import org.darkowl.bash_doc.output.text.BashDocTextOutput;
import org.junit.jupiter.api.Test;

class VersionHistoryDataProcessorTest {

    BashDocTextOutput obj = new BashDocTextOutput();

    @Test
    void testProcess() {
        for (int i = 0; i < 6; i++) {
            StringBuilder sb = new StringBuilder();
            List<VersionHistoryData> data = null;
            VersionHistoryDataProcessor.process(obj, sb, i, data);
            assertEquals("", sb.toString());

            data = new ArrayList<>();

            VersionHistoryDataProcessor.process(obj, sb, i, data);
            assertEquals("", sb.toString());

            data.add(null);
            VersionHistoryDataProcessor.process(obj, sb, i, data);
            assertEquals("", sb.toString());

            final VersionHistoryData item = new VersionHistoryData();
            data.add(item);
            VersionHistoryDataProcessor.process(obj, sb, i, data);
            assertEquals("", sb.toString());

            item.setVersion(" ");
            VersionHistoryDataProcessor.process(obj, sb, i, data);
            assertEquals("", sb.toString());

            item.setVersion("1.0.0");
            VersionHistoryDataProcessor.process(obj, sb, i, data);
            TestUtils.testPerLine(sb.toString().replace("*", ""), "", "Version History", "", "", "", "1.0.0", "");

            sb = new StringBuilder();
            item.setAuthor("Author1");
            item.setAuthorEmail("Author1@email.com");
            VersionHistoryDataProcessor.process(obj, sb, i, data);
            TestUtils.testPerLine(
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
            VersionHistoryDataProcessor.process(obj, sb, i, data);
            TestUtils.testPerLine(
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
            VersionHistoryDataProcessor.process(obj, sb, 0, data);
            TestUtils.testPerLine(
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
}
