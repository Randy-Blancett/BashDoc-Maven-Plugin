package org.darkowl.bash_doc.output;

import java.util.List;

import org.darkowl.bash_doc.model.VariableData;

public class VariableDataProcessor extends SortedBaseDataProcessor<VariableData> {
    private static VariableDataProcessor instance = null;

    public static void process(final OutputFormatter outputFormatter,
            final StringBuilder sb,
            final int i,
            final List<VariableData> variables) {
        if (instance == null)
            instance = new VariableDataProcessor();
        instance.format(outputFormatter, sb, i, variables);
    }

    @Override
    protected void formatData(final OutputFormatter outputFormatter,
            final StringBuilder output,
            final int index,
            final VariableData var) {
        outputFormatter.process(output, index + 1, var);
    }

    @Override
    protected String getHeaderString() {
        return "Variables";
    }

    @Override
    protected boolean isValid(final VariableData var) {
        return var != null && var.getName() != null && !var.getName().isBlank();
    }

}
