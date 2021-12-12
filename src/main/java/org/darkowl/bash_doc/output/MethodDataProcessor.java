package org.darkowl.bash_doc.output;

import java.util.List;

import org.darkowl.bash_doc.model.MethodData;

public class MethodDataProcessor extends SortedBaseDataProcessor<MethodData> {
    private static MethodDataProcessor instance = null;

    public static void process(final OutputFormatter outputFormatter,
            final StringBuilder sb,
            final int i,
            final List<MethodData> methods) {
        if (instance == null)
            instance = new MethodDataProcessor();
        instance.format(outputFormatter, sb, i, methods);
    }

    @Override
    protected void formatData(final OutputFormatter outputFormatter,
            final StringBuilder output,
            final int index,
            final MethodData method) {
        outputFormatter.process(output, index + 1, method);
    }

    @Override
    protected String getHeaderString() {
        return "Methods";
    }

    @Override
    protected boolean isValid(final MethodData data) {
        return data != null && data.getName() != null && !data.getName().isBlank();
    }

}
