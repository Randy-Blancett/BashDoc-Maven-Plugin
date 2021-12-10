package org.darkowl.bash_doc.output;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;

import org.darkowl.bash_doc.enums.OutputType;
import org.junit.jupiter.api.Test;

class OutputManagerTest {

    @Test
    void testInit() {
        OutputManager.init(null);
        assertFalse(OutputManager.isOutput(OutputType.RAW_XML));
        OutputManager.init(Map.of(OutputType.RAW_XML, false));
        assertFalse(OutputManager.isOutput(null));
        assertFalse(OutputManager.isOutput(OutputType.RAW_XML));
        assertFalse(OutputManager.isOutput(OutputType.TEXT));
        OutputManager.init(Map.of(OutputType.RAW_XML, true));
        assertTrue(OutputManager.isOutput(OutputType.RAW_XML));
    }

    @Test
    void testOutput() {
//        fail("Not yet implemented");
    }

}
