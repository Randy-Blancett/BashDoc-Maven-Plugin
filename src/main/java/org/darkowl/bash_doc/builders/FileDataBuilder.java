package org.darkowl.bash_doc.builders;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Stack;

import org.darkowl.bash_doc.enums.LineTags;
import org.darkowl.bash_doc.model.CommonCommentData;
import org.darkowl.bash_doc.model.ComponentCommentData;
import org.darkowl.bash_doc.model.FileData;
import org.darkowl.bash_doc.model.ScopeType;
import org.darkowl.bash_doc.model.VariableData;
import org.darkowl.bash_doc.model.VersionHistoryData;

public class FileDataBuilder {

    static class CommentStack extends Stack<StackObj<?>> {

        /**
         *
         */
        private static final long serialVersionUID = 6921142743359838251L;

        @Override
        public synchronized StackObj<?> peek() {
            if (isEmpty())
                return null;
            return super.peek();
        }

    }

    static class StackObj<T> {
        private final T data;

        private final LineTags type;

        public StackObj(final LineTags type, final T data) {
            this.type = type;
            this.data = data;
        }

        public T getData() {
            return data;
        }

        public LineTags getType() {
            return type;
        }
    }

    private static String buildComment(final String currentComment, final String newComment) {
        final StringBuilder sb = new StringBuilder();
        if (currentComment != null && !currentComment.isBlank())
            sb.append(currentComment).append('\n');

        if (newComment != null && !newComment.isBlank())
            sb.append(newComment);

        return sb.toString();

    }

    public static FileData create(final Path file) throws IOException {
        final CommentStack commentStack = new CommentStack();
        final FileData output = new FileData();
        output.setFileName(file.getFileName().toString());
        for (final String line : Files.readAllLines(file, StandardCharsets.UTF_8)) {
            final LineTags lineType = LineTags.parse(line);
            popStack(commentStack, lineType);
            final String data = lineType.stripTag(line);
            switch (lineType) {
            case AUTHOR:
                processAuthor(commentStack.peek(), data);
                break;
            case AUTHOR_EMAIL:
                processAuthor_Email(commentStack.peek(), data);
                break;
            case FILE:
                setStack(commentStack, new StackObj<>(LineTags.FILE, output));
                break;
            case VARIABLE:
                processVariable(commentStack, output.getVariable());
                break;
            case VERSION:
                processVersion(commentStack.peek(), data);
                break;
            case VERSIONS:
                setStack(commentStack, new StackObj<>(LineTags.VERSIONS, output.getVersionHistory()));
                break;
            case RELEASE:
                processRelease(commentStack.peek(), data);
                break;
            case HISTORIC_VERSION:
                processHistoricVersion(commentStack, data);
                break;
            case COMMENT:
                // need to wrap comment stack in an object that handles empty stack
                processComment(commentStack.peek(), data);
                break;
            case CODE:
                process(commentStack, data);
                break;
            case PUBLIC:
                process(commentStack.peek(), ScopeType.PUBLIC);
                break;
            case PRIVATE:
                process(commentStack.peek(), ScopeType.PRIVATE);
                break;
            case PROTECTED:
                process(commentStack.peek(), ScopeType.PROTECTED);
                break;
            default:
                break;

            }
        }
        return output;
    }

    private static void popStack(final Stack<StackObj<?>> stack, final LineTags lineType) {
        if (stack == null || lineType == null)
            return;
        while (lineType.getLevel() <= stack.size())
            stack.pop();
    }

    private static void process(final CommentStack commentStack, final String data) {
        if (commentStack == null || data == null || data.isBlank())
            return;
        final StackObj<?> obj = commentStack.peek();
        if (obj == null)
            return;
        switch (obj.getType()) {
        case VARIABLE:
            if (!(obj.getData() instanceof VariableData)) {
                System.out.println("Data type is not Variable and should be...");
                return;
            }
            process((VariableData) obj.getData(), data);
            commentStack.pop();
            break;
        default:
            break;
        }
    }

    private static void process(final StackObj<?> obj, final ScopeType data) {
        if (obj == null)
            return;
        switch (obj.getType()) {
        case VARIABLE:
            ((ComponentCommentData) obj.getData()).setScope(data);
            break;
        }

    }

    private static void process(final VariableData output, final String data) {
        if (data == null || data.isBlank() || output == null)
            return;
        final String[] array = data.split("=");
        output.setName(array[0]);
        if (array.length > 1)
            output.setDefault(array[1]);
    }

    private static void processAuthor(final StackObj<?> obj, final String data) {
        if (obj == null)
            return;
        switch (obj.getType()) {
        case AUTHOR:
            break;
        case AUTHOR_EMAIL:
            break;
        case CODE:
            break;
        case COMMENT:
            break;
        case VARIABLE:
        case HISTORIC_VERSION:
        case FILE:
            ((CommonCommentData) obj.getData()).setAuthor(data);
            break;
        case VERSION:
            break;
        case VERSIONS:
            break;
        default:
            break;
        }
    }

    private static void processAuthor_Email(final StackObj<?> obj, final String data) {
        if (obj == null)
            return;
        switch (obj.getType()) {
        case AUTHOR:
            break;
        case AUTHOR_EMAIL:
            break;
        case CODE:
            break;
        case COMMENT:
            break;
        case VARIABLE:
        case HISTORIC_VERSION:
        case FILE:
            ((CommonCommentData) obj.getData()).setAuthorEmail(data);
            break;
        case VERSION:
            break;
        case VERSIONS:
            break;
        case PRIVATE:
            break;
        case PROTECTED:
            break;
        case PUBLIC:
            break;
        case RELEASE:
            break;
        default:
            break;
        }
    }

    private static void processComment(final StackObj<?> obj, final String comment) {
        if (obj == null || comment == null || comment.isBlank())
            return;
        switch (obj.getType()) {
        case AUTHOR:
            break;
        case AUTHOR_EMAIL:
            break;
        case CODE:
            break;
        case COMMENT:
            break;
        case HISTORIC_VERSION: {
            final CommonCommentData data = (CommonCommentData) obj.getData();
            data.setComment(buildComment(data.getComment(), " - " + comment));
            break;
        }
        case VARIABLE:
        case FILE: {
            final CommonCommentData data = (CommonCommentData) obj.getData();
            data.setComment(buildComment(data.getComment(), comment));
            break;
        }
        case VERSION:
            break;
        case VERSIONS:
            break;
        default:
            break;
        }

    }

    private static void processHistoricVersion(final Stack<StackObj<?>> stack, final String data) {
        final StackObj<?> obj = stack.peek();
        if (obj == null)
            return;
        switch (obj.getType()) {
        case AUTHOR:
            break;
        case AUTHOR_EMAIL:
            break;
        case CODE:
            break;
        case COMMENT:
            break;
        case FILE:
            break;
        case HISTORIC_VERSION:
            stack.pop();
            processHistoricVersion(stack, data);
            break;
        case VERSIONS:
            @SuppressWarnings("unchecked")
            final List<VersionHistoryData> list = (List<VersionHistoryData>) obj.getData();
            final VersionHistoryData history = new VersionHistoryData();
            history.setVersion(data);
            list.add(history);
            setStack(stack, new StackObj<>(LineTags.HISTORIC_VERSION, history));
            break;
        case VERSION:
            break;
        default:
            break;
        }

    }

    private static void processRelease(final StackObj<?> obj, final String data) {
        if (obj == null)
            return;
        switch (obj.getType()) {
        case HISTORIC_VERSION:
            ((VersionHistoryData) obj.getData()).setRelease(data);
            break;
        default:
            break;
        }
    }

    private static void processVariable(final Stack<StackObj<?>> stack, final List<VariableData> data) {
        final VariableData obj = new VariableData();
        data.add(obj);
        setStack(stack, new StackObj<>(LineTags.VARIABLE, obj));
    }

    private static void processVersion(final StackObj<?> obj, final String version) {
        if (obj == null)
            return;
        switch (obj.getType()) {
        case AUTHOR:
            break;
        case AUTHOR_EMAIL:
            break;
        case CODE:
            break;
        case COMMENT:
            break;
        case FILE:
            ((FileData) obj.getData()).setVersion(version);
            break;
        case HISTORIC_VERSION:
            break;
        case VERSION:
            break;
        case VERSIONS:
            break;
        default:
            break;
        }
    }

    static void setStack(final Stack<StackObj<?>> commentStack, final StackObj<?> obj) {
        if (commentStack == null || obj == null)
            return;
        while (obj.getType().getLevel() <= commentStack.size())
            commentStack.pop();
        commentStack.push(obj);
    }

}
