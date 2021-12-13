package org.darkowl.bash_doc.output;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class BaseDataProcessorTest extends BaseDataProcessor<String> {

    @Override
    protected void formatData(final OutputFormatter outputFormatter,
            final StringBuilder output,
            final int index,
            final String data) {
        // TODO Auto-generated method stub

    }

    @Override
    protected String getHeaderString() {
        // TODO Auto-generated method stub
        return null;
    }

    @Test
    void test() {
        assertTrue(isValid("Hello"));
        assertFalse(isValid(null));
    }

}
