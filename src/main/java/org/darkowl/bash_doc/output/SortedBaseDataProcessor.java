package org.darkowl.bash_doc.output;

import java.util.Collections;
import java.util.List;

import org.darkowl.bash_doc.model.ComponentCommentData;
import org.darkowl.bash_doc.output.text.ComponentCommentDataSort;

public abstract class SortedBaseDataProcessor<T extends ComponentCommentData> extends BaseDataProcessor<T> {

    private static final ComponentCommentDataSort COMPONENT_COMMENT_DATA_SORTER = new ComponentCommentDataSort();

    @Override
    protected void format(final OutputFormatter outputFormatter,
            final StringBuilder output,
            final int index,
            final List<T> dataItems) {
        if (dataItems == null || dataItems.isEmpty())
            return;
        boolean isFirst = true;
        Collections.sort(dataItems, COMPONENT_COMMENT_DATA_SORTER);
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

}
