package org.darkowl.bash_doc.output;

import java.util.List;

import org.darkowl.bash_doc.model.ExitCodeData;

public class ExitCodeDataProcessor extends BaseDataProcessor<ExitCodeData> {
    private static ExitCodeDataProcessor instance = null;

    public static void process(final OutputFormatter outputFormatter,
            final StringBuilder sb,
            final int i,
            final List<ExitCodeData> data) {
        if (instance == null)
            instance = new ExitCodeDataProcessor();
        instance.format(outputFormatter, sb, i, data);
    }

    @Override
    protected void formatData(final OutputFormatter outputFormatter,
            final StringBuilder output,
            final int index,
            final ExitCodeData code) {
        output.append(outputFormatter.createExitCodeOutput(index, code.getCode(), code.getDescription()));
    }

    @Override
    protected String getHeaderString() {
        return "Exit Codes";
    }

    @Override
    protected boolean isValid(final ExitCodeData code) {
        return code.getCode() != null || code.getDescription() != null && code.getDescription().isBlank();
    }

}
