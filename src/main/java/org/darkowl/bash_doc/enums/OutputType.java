package org.darkowl.bash_doc.enums;

import org.darkowl.bash_doc.output.OutputFormatter;
import org.darkowl.bash_doc.output.markdown.OutputMarkdown;
import org.darkowl.bash_doc.output.rawXml.OutputRawXml;
import org.darkowl.bash_doc.output.text.BashDocTextOutput;

public enum OutputType {
    MARKDOWN(new OutputMarkdown()),
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
