package org.darkowl.bash_doc.output;

import java.io.IOException;
import java.nio.file.Path;
import java.text.DateFormat;

import org.apache.maven.plugin.logging.Log;
import org.darkowl.bash_doc.model.CommonCommentData;
import org.darkowl.bash_doc.model.Library;
import org.darkowl.bash_doc.model.MethodData;
import org.darkowl.bash_doc.model.VariableData;

public abstract class OutputFormatter {
    private static final DateFormat dateFormater = DateFormat.getDateTimeInstance();
    private Log log;

    private Path outputDir;

    public void addHeader(final StringBuilder sb, final int i, final String string, final String createdString) {
    }

    public String createExitCodeOutput(final int indent, final Integer code, final String description) {
        return "";
    }

    protected String createParameterOutput(final int indent,
            final Integer position,
            final String name,
            final String description) {
        return "";
    }

    public Log getLog() {
        return log;
    }

    public Path getOutputDir() {
        return outputDir;
    }

    protected void outputLine(final StringBuilder output, final int indent, final String... data) {
    }

    protected void process(final Library library) {
        log.info("Processing Output...");
        if (library == null)
            return;
        final String createdString = library.getCreated() == null ? "" : dateFormater.format(library.getCreated());
        library.getFiles().forEach(file -> {
            if (file == null)
                return;
            log.debug("Processing file: " + file.getFileName());
            final StringBuilder sb = new StringBuilder();
            addHeader(sb, 0, file.getFileName() + " (" + file.getVersion() + ")", createdString);
            process(sb, 0, file);
            VersionHistoryDataProcessor.process(this, sb, 1, file.getVersionHistory());
            ExitCodeDataProcessor.process(this, sb, 1, file.getExitCode());
            VariableDataProcessor.process(this, sb, 1, file.getVariable());
            MethodDataProcessor.process(this, sb, 1, file.getMethod());
            writeFileData(file.getFileName(), sb.toString().getBytes());
        });
    }

    public void process(final Log log, final Path outputDir, final Library library) throws IOException {
        this.outputDir = outputDir.resolve("text");
        this.log = log;
    }

    protected void process(final StringBuilder sb, final int i, final CommonCommentData commentData) {
    }

    protected void process(final StringBuilder output, final int index, final MethodData data) {
        if (data == null || data.getName() == null || data.getName().isBlank())
            return;
        addHeader(output, index, data.getName(), data.getScope() == null ? null : data.getScope().value());
        process(output, index, (CommonCommentData) data);
        ParameterDataProcessor.process(this, output, index + 1, data.getParameter());
        processReturn(output, index + 1, data.getReturn());
        ExitCodeDataProcessor.process(this, output, index + 1, data.getExitCode());
        ExampleProcessor.process(this, output, index + 1, data.getExample());
    }

    protected void process(final StringBuilder output, final int index, final VariableData data) {
    }

    protected void processReturn(final StringBuilder sb, final int indent, final String description) {
    }

    protected void writeFileData(final String replaceFirst, final byte[] bytes) {
    }

}
