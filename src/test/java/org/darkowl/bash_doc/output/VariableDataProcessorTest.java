package org.darkowl.bash_doc.output;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.darkowl.bash_doc.TestUtils;
import org.darkowl.bash_doc.model.ScopeType;
import org.darkowl.bash_doc.model.VariableData;
import org.darkowl.bash_doc.model.VariableType;
import org.darkowl.bash_doc.output.text.BashDocTextOutput;
import org.junit.jupiter.api.Test;

class VariableDataProcessorTest {

    BashDocTextOutput obj = new BashDocTextOutput();

    @Test
    void testProcessVariables() {
        for (int i = 0; i < 6; i++) {
            StringBuilder sb = new StringBuilder();
            List<VariableData> data = null;
            VariableDataProcessor.process(obj, sb, i, data);
            assertEquals("", sb.toString());

            data = new ArrayList<>();
            VariableDataProcessor.process(obj, sb, i, data);
            assertEquals("", sb.toString());

            data.add(null);
            VariableDataProcessor.process(obj, sb, i, data);
            assertEquals("", sb.toString());

            VariableData item = new VariableData();
            data.add(item);
            VariableDataProcessor.process(obj, sb, i, data);
            assertEquals("", sb.toString());

            item.setName(" ");
            VariableDataProcessor.process(obj, sb, i, data);
            assertEquals("", sb.toString());

            item.setName("Var1");
            VariableDataProcessor.process(obj, sb, i, data);
            TestUtils.testPerLine(sb.toString().replace("*", "").trim(), "Variables", "", "", "", "Var1");

            sb = new StringBuilder();
            item = new VariableData();
            data.add(item);
            item.setName("Var2");
            item.setAuthor("Author 1");
            item.setAuthorEmail("Author1@email.com");
            item.setComment("This is the Second Variable.");
            item.setDefault("Data");
            item.setType(VariableType.STRING);
            VariableDataProcessor.process(obj, sb, i, data);
            TestUtils.testPerLine(
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
            VariableDataProcessor.process(obj, sb, 0, data);
            TestUtils.testPerLine(
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

}
