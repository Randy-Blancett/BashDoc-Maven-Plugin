package org.darkowl.bash_doc.enums;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class LineTagsTest {

    @ParameterizedTest
    @CsvSource({ " #FILE          , FILE", //
            "data            , CODE", //
            " #BAD            , COMMENT", //
            " # Comment       , COMMENT", //
            " #VARIABLE       , VARIABLE" })
    void testParse(final String line, final LineTags tag) {
        assertEquals(tag, LineTags.parse(line));
    }

    @ParameterizedTest
    @CsvSource({ " #VERSION 1.0.0          , VERSION, 1.0.0" })
    void testStripTag(final String line, final LineTags tag, final String expected) {
        assertEquals(expected, tag.stripTag(line));
    }

}
