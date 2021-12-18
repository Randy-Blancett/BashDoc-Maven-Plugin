package org.darkowl.bash_doc.output.markdown;

import org.darkowl.bash_doc.model.CommonCommentData;
import org.darkowl.bash_doc.model.VariableData;
import org.darkowl.bash_doc.output.OutputFormatter;

public class OutputMarkdown extends OutputFormatter {

    static String createHeaderData(final int indent, final String text, final String tailText) {
        if ((text == null || text.isBlank()) && (tailText == null || tailText.isBlank()))
            return "";
        final StringBuilder output = new StringBuilder();
        indentLine(output, indent);
        output.append(text);
        if (tailText != null && !tailText.isBlank())
            output.append("<span style=\"float:right;\"> " + tailText + " </span>");
        output.append("\n");
        return output.toString();
    }

    static String createPropertyOutput(final int indent, final String key, final String value) {
        if (key == null || key.isBlank() || value == null || value.isBlank())
            return "";

        return "* **" + key + "**: " + value + "\n";
    }

    private static void indentLine(final StringBuilder output, final int indent) {
        for (int i = -1; i < indent; i++)
            output.append('#');
        output.append(' ');
    }

    public OutputMarkdown() {
        super("markdown", "md");
    }

    @Override
    public void addHeader(final StringBuilder sb, final int indent, final String text, final String tailText) {
        if (sb.length() > 1)
            sb.append('\n');
        sb.append(createHeaderData(indent, text, tailText));
    }

    String createCommentBlock(final int indent, final String comment) {
        if (comment == null || comment.isBlank())
            return "";
        final String[] commentLines = comment.split("\n");
        final StringBuilder output = new StringBuilder();
        int lineCount = 0;
        for (final String line : commentLines) {
            if (line == null || line.isBlank())
                continue;
            if (lineCount > 0)
                output.append("<br/>\n");
            output.append(line.trim());
            lineCount++;
        }
        output.append("\n\n");

        return output.toString();
    }

    @Override
    public String createExitCodeOutput(final int indent, final Integer code, final String description) {
        if (code == null && description == null)
            return "";
        return (code == null ? "" : String.format("**%2d** - ", code)) + (description == null ? "" : description)
                + "<br/>\n";
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
    protected String createParameterOutput(final int indent,
            final Integer position,
            final String name,
            final String description) {
        if (position == null && name == null && description == null)
            return null;
        final StringBuilder output = new StringBuilder();
        outputLine(output, indent, position == null ? "" : String.format("%02d - ", position), name, description);
        return output.toString();
    }

    @Override
    protected void outputLine(final StringBuilder output, final int indent, final String... data) {
        final StringBuilder sb = new StringBuilder();
        for (final String item : data) {
            if (item == null || item.isBlank())
                continue;
            sb.append(' ');
            sb.append(item);

        }
        if (sb.length() > 0)
            output.append("* ").append(sb).append('\n');
    }

    @Override
    protected void process(final StringBuilder output, final int index, final CommonCommentData commentData) {
        if (commentData == null)
            return;
        output.append(createCommentBlock(index, commentData.getComment()))
                .append(createPropertyOutput(index, "Author", commentData.getAuthor()))
                .append(createPropertyOutput(index, "Author Email", commentData.getAuthorEmail()));
    }

}
