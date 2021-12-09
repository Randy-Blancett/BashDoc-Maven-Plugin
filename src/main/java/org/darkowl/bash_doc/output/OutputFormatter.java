package org.darkowl.bash_doc.output;

import java.io.IOException;
import java.nio.file.Path;
import java.text.DateFormat;
import java.util.List;

import org.apache.maven.plugin.logging.Log;
import org.darkowl.bash_doc.model.ExitCodeData;
import org.darkowl.bash_doc.model.FileData;
import org.darkowl.bash_doc.model.Library;
import org.darkowl.bash_doc.model.MethodData;
import org.darkowl.bash_doc.model.VariableData;
import org.darkowl.bash_doc.model.VersionHistoryData;

public abstract class OutputFormatter {
    private Log log;
    private Path outputDir;

    public Log getLog() {
        return log;
    }

    public Path getOutputDir() {
        return outputDir;
    }

    public void process(final Log log, final Path outputDir, final Library library) throws IOException {
        this.outputDir = outputDir.resolve("text");
        this.log = log;
    }

    private static final DateFormat dateFormater = DateFormat.getDateTimeInstance();

    protected void process(final Library library) {
        log.info("Processing Text Output...");
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
            process(sb, 1, file.getVersionHistory());
            processExitCodes(sb, 1, file.getExitCode());
            processVariables(sb, 1, file.getVariable());
            processMethods(sb, 1, file.getMethod());
            writeFileData(file.getFileName(), sb.toString().getBytes());
        });
    }

    protected void writeFileData(String replaceFirst, byte[] bytes) {
    }

    protected void processMethods(StringBuilder sb, int i, List<MethodData> method) {
    }

    protected void processVariables(StringBuilder sb, int i, List<VariableData> variable) {
    }

    protected void processExitCodes(StringBuilder sb, int i, List<ExitCodeData> exitCode) {
    }

    protected void process(StringBuilder sb, int i, List<VersionHistoryData> versionHistory) {
    }

    protected void process(StringBuilder sb, int i, FileData file) {}

    protected void addHeader(StringBuilder sb, int i, String string, String createdString) {
    }
}
