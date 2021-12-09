package org.darkowl.bash_doc.enums;

import org.darkowl.bash_doc.output.OutputFormatter;
import org.darkowl.bash_doc.output.rawXml.OutputRawXml;
import org.darkowl.bash_doc.output.text.BashDocTextOutput;

public enum OutputType {
    RAW_XML(new OutputRawXml()),
    TEXT(null);

    private final OutputFormatter formatter;

    OutputType(OutputRawXml object) {
        formatter = object;
    }

    public OutputFormatter getFormatter() {
        return formatter;
    }
}
