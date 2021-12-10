package org.darkowl.bash_doc.output.text;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.maven.plugin.logging.Log;
import org.darkowl.bash_doc.model.CommonCommentData;
import org.darkowl.bash_doc.model.Library;
import org.darkowl.bash_doc.model.VariableData;
import org.darkowl.bash_doc.output.OutputFormatter;

public class BashDocTextOutput extends OutputFormatter {
    public static final int INDENT_SIZE = 4;
    public static final int LINE_WIDTH = 80;

    private static String createHeaderData(final int indent, final String text, final String tailText) {
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

    static String createPropertyOutput(final int indent, final String key, final String value) {
        if (key == null || value == null)
            return "";
        final StringBuilder output = new StringBuilder();
        outputLine(output, indent, false, " -", key, ":", value);
        return output.toString();
    }

    private static void indentLine(final StringBuilder output, final int indent) {
        for (int i = 0; i < indent; i++)
            for (int i2 = 0; i2 < INDENT_SIZE; i2++)
                output.append(' ');
    }

    static void outputLine(final StringBuilder output,
            final int indent,
            final boolean processSpaces,
            final String... data) {
        long start = output.length();
        indentLine(output, indent);
        boolean hasOutput = false;
        for (final String item : data) {
            if (item == null || item.isBlank())
                continue;
            String[] words;
            if (processSpaces)
                words = item.split("\\b");
            else {
                words = new String[1];
                words[0] = item;
            }
            for (final String word : words) {
                if (word == null || word.isBlank())
                    continue;
                final String cleanWord = processSpaces ? word.trim() : word;
                if (hasOutput && output.length() - start + cleanWord.length() + 1 > LINE_WIDTH) {
                    if (cleanWord.isBlank())
                        continue;
                    output.append('\n');
                    start = output.length();
                    indentLine(output, indent + 1);
                    hasOutput = false;
                }
                if (hasOutput && Character.isLetterOrDigit(cleanWord.charAt(0)))
                    output.append(' ');
                output.append(cleanWord);
                hasOutput = true;
            }
        }
        output.append('\n');
    }

    private static String padRight(final String text, final int totalLength) {
        final StringBuilder output = new StringBuilder(text == null ? "" : text);
        while (output.length() < totalLength)
            output.append(' ');
        return output.toString();
    }

    @Override
    public void addHeader(final StringBuilder sb, final int indent, final String text, final String tailText) {
        if (sb.length() > 1)
            sb.append('\n');
        sb.append(createHeaderLine(indent)).append(createHeaderData(indent, text, tailText))
                .append(createHeaderLine(indent));
    }

    String createCommentBlock(final int indent, final String comment) {
        if (comment == null || comment.isBlank())
            return "";
        final String[] commentLines = comment.split("\n");
        final StringBuilder output = new StringBuilder();
        for (final String line : commentLines) {
            if (line == null || line.isBlank())
                continue;
            outputLine(output, indent, line);
        }

        return output.toString();
    }

    @Override
    public String createExitCodeOutput(final int indent, final Integer code, final String description) {
        if (code == null && description == null)
            return null;
        final StringBuilder output = new StringBuilder();
        outputLine(output, indent, " ", code == null ? "" : String.format("%2d - ", code), description);
        return output.toString();
    }

    @Override
    protected String createParameterOutput(final int indent,
            final Integer position,
            final String name,
            final String description) {
        if (position == null && name == null && description == null)
            return null;
        final StringBuilder output = new StringBuilder();
        outputLine(
                output,
                indent,
                false,
                " ",
                position == null ? "" : String.format("%02d -", position),
                padRight(name, 15),
                description);
        return output.toString();
    }

    @Override
    protected void outputLine(final StringBuilder output, final int indent, final String... data) {
        outputLine(output, indent, true, data);
    }

    @Override
    public void process(final Log log, final Path outputDir, final Library library) throws IOException {
        super.process(log, outputDir, library);
        Files.createDirectories(getOutputDir());
        process(library);
    }

    @Override
    protected void process(final StringBuilder output, final int index, final CommonCommentData commentData) {
        if (commentData == null)
            return;
        output.append(createCommentBlock(index, commentData.getComment()))
                .append(createPropertyOutput(index, "Author", commentData.getAuthor()))
                .append(createPropertyOutput(index, "Author Email", commentData.getAuthorEmail()));
    }

    @Override
    protected void process(final StringBuilder output, final int index, final VariableData data) {
        if (data == null)
            return;
        addHeader(output, index, data.getName(), data.getScope() == null ? null : data.getScope().value());
        process(output, index, (CommonCommentData) data);
        output.append(createPropertyOutput(index, "Default Value", data.getDefault()));
    }

    @Override
    protected void processReturn(final StringBuilder sb, final int indent, final String description) {
        if (description == null || description.isEmpty())
            return;
        addHeader(sb, indent, "Return", null);
        outputLine(sb, indent, description);
    }

    @Override
    protected void writeFileData(final String fileName, final byte[] content) {
        final String localFileName = fileName.replaceFirst("[.][^.]+$", ".txt");
        final Path filePath = getOutputDir().resolve(localFileName);
        getLog().debug("Saving data to: " + filePath.toAbsolutePath());
        try {
            Files.write(filePath, content);
        } catch (final IOException e) {
            getLog().error("Failed to write text document", e);
        }

    }
}
