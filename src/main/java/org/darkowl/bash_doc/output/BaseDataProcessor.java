package org.darkowl.bash_doc.output;

import java.util.List;

public abstract class BaseDataProcessor<T> {
    protected void format(final OutputFormatter outputFormatter,
            final StringBuilder output,
            final int index,
            final List<T> dataItems) {
        if (dataItems == null || dataItems.isEmpty())
            return;
        boolean isFirst = true;
        for (final T data : dataItems) {
            if (!isValid(data))
                continue;
            if (isFirst) {
                outputFormatter.addHeader(output, index, getHeaderString(), null);
                isFirst = false;
            }
            formatData(outputFormatter, output, index, data);
        }
    }

    protected abstract void formatData(OutputFormatter outputFormatter,
            final StringBuilder output,
            final int index,
            T data);

    protected abstract String getHeaderString();

    protected boolean isValid(final T data) {
        return data != null;
    }
}
