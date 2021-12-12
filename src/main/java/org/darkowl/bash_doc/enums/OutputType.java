package org.darkowl.bash_doc.enums;

import org.darkowl.bash_doc.output.OutputFormatter;
import org.darkowl.bash_doc.output.rawXml.OutputRawXml;
import org.darkowl.bash_doc.output.text.BashDocTextOutput;

public enum OutputType {
    RAW_XML(new OutputRawXml()),
    TEXT(new BashDocTextOutput());

    private final OutputFormatter formatter;

    OutputType(final OutputFormatter object) {
        formatter = object;
    }

    public OutputFormatter getFormatter() {
        return formatter;
    }
}
