package org.darkowl.bash_doc.output;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.darkowl.bash_doc.TestUtils;
import org.darkowl.bash_doc.model.ParameterData;
import org.darkowl.bash_doc.output.text.BashDocTextOutput;
import org.junit.jupiter.api.Test;

class ParameterDataProcessorTest {

    BashDocTextOutput obj = new BashDocTextOutput();

    @Test
    void testProcessParameters() {
        for (int i = 0; i < 6; i++) {
            StringBuilder sb = new StringBuilder();
            List<ParameterData> data = null;
            ParameterDataProcessor.process(obj, sb, i, data);
            assertEquals("", sb.toString());

            data = new ArrayList<>();
            ParameterDataProcessor.process(obj, sb, i, data);
            assertEquals("", sb.toString());

            data.add(null);
            ParameterDataProcessor.process(obj, sb, i, data);
            assertEquals("", sb.toString());

            ParameterData item = new ParameterData();
            data.add(item);
            ParameterDataProcessor.process(obj, sb, i, data);
            assertEquals("", sb.toString());

            item.setPosition(1);
            ParameterDataProcessor.process(obj, sb, i, data);
            TestUtils.testPerLine(sb.toString().replace("*", "").trim(), "Parameters", "", "01 -");

            sb = new StringBuilder();
            data = new ArrayList<>();
            item = new ParameterData();
            data.add(item);
            ParameterDataProcessor.process(obj, sb, i, data);
            assertEquals("", sb.toString());

            item.setName(" ");
            ParameterDataProcessor.process(obj, sb, i, data);
            assertEquals("", sb.toString());

            item.setName("Var1");
            ParameterDataProcessor.process(obj, sb, i, data);
            TestUtils.testPerLine(sb.toString().replace("*", "").trim(), "Parameters", "", "Var1");

            sb = new StringBuilder();
            data = new ArrayList<>();
            item = new ParameterData();
            data.add(item);

            item.setDescrtiption(" ");
            ParameterDataProcessor.process(obj, sb, i, data);
            assertEquals("", sb.toString());

            item.setDescrtiption("Description");
            ParameterDataProcessor.process(obj, sb, i, data);
            TestUtils.testPerLine(sb.toString().replace("*", "").trim(), "Parameters", "", "Description");

            sb = new StringBuilder();
            data = new ArrayList<>();
            item = new ParameterData();
            data.add(item);

            item.setPosition(1);
            item.setName("Var 1");
            item.setDescrtiption("Description");

            item = new ParameterData();
            data.add(item);
            item.setPosition(2);
            item.setName("Var 2");
            item.setDescrtiption("Description2");
            ParameterDataProcessor.process(obj, sb, i, data);
            TestUtils.testPerLine(
                    sb.toString().replace("*", "").trim(),
                    "Parameters",
                    "",
                    "01 - Var 1           Description",
                    "02 - Var 2           Description2");
        }
    }

}
