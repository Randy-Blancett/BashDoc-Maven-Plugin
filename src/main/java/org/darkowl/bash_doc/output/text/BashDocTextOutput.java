package org.darkowl.bash_doc.output.text;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DateFormat;
import java.util.List;

import org.apache.maven.plugin.logging.Log;
import org.darkowl.bash_doc.model.CommonCommentData;
import org.darkowl.bash_doc.model.Library;
import org.darkowl.bash_doc.model.VersionHistoryData;

public class BashDocTextOutput {
    public static final int LINE_WIDTH = 80;
    private static final DateFormat dateFormater = DateFormat.getDateTimeInstance();

    private static void addHeader(final StringBuilder sb, final int indent, final String text, String tailText) {
        sb.append(createHeaderLine(indent));
        sb.append(createHeaderData(indent, text, tailText));
        sb.append(createHeaderLine(indent));
    }

    private static String createCommentBlock(final int indent, final String comment) {
        if (comment == null)
            return "";
        final String[] commentLines = comment.split("\n");
        final StringBuilder output = new StringBuilder();
        for (final String line : commentLines) {
            if (line == null || line.isBlank())
                continue;
            indentLine(output, indent);
            output.append(line);
            output.append('\n');
        }

        return output.toString();
    }

    private static String createHeaderData(final int indent, final String text, String tailText) {
        final StringBuilder output = new StringBuilder();
        indentLine(output, indent);
        output.append("* " + text);
        int endSpace = 1;
        if (tailText != null)
            endSpace += tailText.length() + 1;
        while (output.length() < LINE_WIDTH - endSpace)
            output.append(' ');
        if (tailText != null)
            output.append(tailText).append(' ');
        output.append("*\n");
        return output.toString();
    }

    private static String createHeaderLine(final int indent) {
        final StringBuilder output = new StringBuilder();
        indentLine(output, indent);
        while (output.length() < LINE_WIDTH)
            output.append('*');
        output.append('\n');
        return output.toString();
    }

    private static String createPropertyOutput(final int indent, final String key, final String value) {
        if (key == null || value == null)
            return "";
        final StringBuilder output = new StringBuilder();
        indentLine(output, indent);
        output.append(" - ").append(key).append(" : ").append(value).append('\n');
        return output.toString();
    }

    private static void indentLine(final StringBuilder output, final int indent) {
        for (int i = 0; i < indent; i++)
            output.append(' ');
    }

    public static void output(final Log log, final Path outputDirectory, final Library library) {
        BashDocTextOutput me;
        try {
            me = new BashDocTextOutput(log, outputDirectory);
            me.process(library);
        } catch (final IOException e) {
            log.error("failed to process text output.", e);
        }
    }

    private final Log log;

    private final Path outputDir;

    public BashDocTextOutput(final Log log, final Path outputDir) throws IOException {
        this.outputDir = outputDir.resolve("text");
        this.log = log;
        Files.createDirectories(this.outputDir);
    }

    private void process(final Library library) {
        log.info("Processing Text Output...");
        String createdString = dateFormater.format(library.getCreated());
        library.getFiles().forEach(file -> {
            if (file == null)
                return;
            log.debug("Processing file: " + file.getFileName());
            final StringBuilder sb = new StringBuilder();
            addHeader(sb, 0, file.getFileName() + " (" + file.getVersion() + ")", createdString);
            process(sb, 0, (CommonCommentData) file);

            process(sb, 1, file.getVersionHistory());
            writeFileData(file.getFileName(), sb.toString().getBytes());
        });
    }

    private void process(StringBuilder output, int index, List<VersionHistoryData> versionHistory) {
        if (versionHistory == null)
            return;
        addHeader(output, index, "Version History", null);
        versionHistory.forEach(version -> {
            if (version == null)
                return;
            addHeader(output, index + 1, version.getVersion(), version.getRelease());
            process(output, index + 1, (CommonCommentData) version);
        });

    }

    private void process(final StringBuilder output, final int index, final CommonCommentData commentData) {
        if (commentData == null)
            return;
        output.append(createCommentBlock(index, commentData.getComment()))
                .append(createPropertyOutput(index, "Author", commentData.getAuthor()))
                .append(createPropertyOutput(index, "Author Email", commentData.getAuthorEmail()));
    }

    private void writeFileData(final String fileName, final byte[] content) {
        final Path filePath = outputDir.resolve(fileName);
        log.debug("Saving data to: " + filePath.toAbsolutePath());
        try {
            Files.write(filePath, content);
        } catch (final IOException e) {
            log.error("Failed to write text document", e);
        }

    }
}
