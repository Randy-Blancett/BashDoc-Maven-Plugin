package org.darkowl.bash_doc.output;

import java.util.List;

import org.darkowl.bash_doc.model.VersionHistoryData;

public class VersionHistoryDataProcessor extends BaseDataProcessor<VersionHistoryData> {
    private static VersionHistoryDataProcessor instance = null;

    public static void process(final OutputFormatter outputFormatter,
            final StringBuilder sb,
            final int i,
            final List<VersionHistoryData> versionHistory) {
        if (instance == null)
            instance = new VersionHistoryDataProcessor();
        instance.format(outputFormatter, sb, i, versionHistory);
    }

    @Override
    protected void formatData(final OutputFormatter outputFormatter,
            final StringBuilder output,
            final int index,
            final VersionHistoryData version) {
        outputFormatter.addHeader(output, index + 1, version.getVersion(), version.getRelease());
        outputFormatter.process(output, index + 1, version);
    }

    @Override
    protected String getHeaderString() {
        return "Version History";
    }

    @Override
    protected boolean isValid(final VersionHistoryData version) {
        return version != null && version.getVersion() != null && !version.getVersion().isBlank();
    }

}
