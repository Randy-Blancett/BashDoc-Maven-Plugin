package org.darkowl.bash_doc;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestUtils {
    public static void testPerLine(final String raw, final String... expected) {
        final String[] data = raw.split("\n");
        assertEquals(expected.length, data.length, "Full Data: " + raw);
        for (int i = 0; i < data.length; i++)
            assertEquals(expected[i], data[i].trim(), "Full Data: " + raw);
    }
}
