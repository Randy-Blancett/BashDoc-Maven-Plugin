package org.darkowl.bash_doc.output;

import java.util.List;

public class ExampleProcessor extends BaseDataProcessor<String> {
    private static ExampleProcessor instance = null;

    public static void process(final OutputFormatter outputFormatter,
            final StringBuilder sb,
            final int i,
            final List<String> params) {
        if (instance == null)
            instance = new ExampleProcessor();
        instance.format(outputFormatter, sb, i, params);
    }

    @Override
    protected void formatData(final OutputFormatter outputFormatter,
            final StringBuilder output,
            final int index,
            final String example) {
        outputFormatter.outputLine(output, index, " ", example);
    }

    @Override
    protected String getHeaderString() {
        return "Examples";
    }

    @Override
    protected boolean isValid(final String example) {
        return example != null && !example.isBlank();
    }

}
