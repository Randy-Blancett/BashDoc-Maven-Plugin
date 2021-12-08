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
import org.darkowl.bash_doc.model.ExitCodeData;
import org.darkowl.bash_doc.model.FileData;
import org.darkowl.bash_doc.model.MethodData;
import org.darkowl.bash_doc.model.ParameterData;
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
            case METHOD:
                processMethod(commentStack, output.getMethod());
                break;
            case EXAMPLES:
                processExamples(commentStack);
                break;
            case EXIT_CODES:
                processExitCodes(commentStack);
                break;
            case RETURN:
                processReturn(commentStack);
                break;
            case RELEASE:
                processRelease(commentStack.peek(), data);
                break;
            case HISTORIC_VERSION:
                processHistoricVersion(commentStack, data);
                break;
            case COMMENT:
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
            case PARAMETERS:
                processParameters(commentStack, data);
                break;
            default:
                break;

            }
        }
        return output;
    }

    private static boolean outputDefaultValue(final String defaultValue) {
        if (defaultValue == null || defaultValue.isBlank() || defaultValue.equals("()"))
            return false;
        return true;
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
        if (obj.getData() instanceof VariableData)
            process((VariableData) obj.getData(), data);
        else if (obj.getData() instanceof MethodData)
            process((MethodData) obj.getData(), data);
        commentStack.pop();
    }

    private static void process(final MethodData output, final String data) {
        if (data == null || data.isBlank() || !data.startsWith("function"))
            return;
        output.setName(data.replaceFirst("function", "").replace("()", "").replace("{", "").trim());
    }

    private static void process(final StackObj<?> obj, final ScopeType data) {
        if (obj == null)
            return;
        if (obj.getData() instanceof ComponentCommentData)
            ((ComponentCommentData) obj.getData()).setScope(data);
        else
            System.out.println("Unknown Data Type " + obj.getData());
    }

    private static void process(final VariableData output, final String data) {
        if (data == null || data.isBlank() || output == null)
            return;
        final String[] array = data.split("=");
        output.setName(array[0]);
        if (array.length > 1 && outputDefaultValue(array[1]))
            output.setDefault(array[1]);
    }

    private static void processAuthor(final StackObj<?> obj, final String data) {
        if (obj == null)
            return;
        if (obj.getData() instanceof CommonCommentData)
            ((CommonCommentData) obj.getData()).setAuthor(data);
        else
            System.out.println("Author is unknown for: " + obj.getData());
    }

    private static void processAuthor_Email(final StackObj<?> obj, final String data) {
        if (obj == null)
            return;
        if (obj.getData() instanceof CommonCommentData)
            ((CommonCommentData) obj.getData()).setAuthorEmail(data);
        else
            System.out.println("Author Email is unknown for: " + obj.getData());
    }

    @SuppressWarnings("unchecked")
    private static void processComment(final StackObj<?> obj, final String comment) {
        if (obj == null || comment == null || comment.isBlank())
            return;
        switch (obj.getType()) {
        case HISTORIC_VERSION: {
            final CommonCommentData data = (CommonCommentData) obj.getData();
            data.setComment(buildComment(data.getComment(), " - " + comment));
            return;
        }
        case PARAMETERS: {
            if (obj.getData() instanceof List)
                processParameters((List<ParameterData>) obj.getData(), comment);
            break;
        }
        case EXAMPLES: {
            if (obj.getData() instanceof List)
                ((List<String>) obj.getData()).add(comment);
            break;
        }
        case EXIT_CODES: {
            if (obj.getData() instanceof List)
                processExitCodes((List<ExitCodeData>) obj.getData(), comment);
            break;
        }
        case RETURN: {
            if (obj.getData() instanceof MethodData)
                ((MethodData) obj.getData()).setReturn(comment);
            break;
        }
        default:
            if (obj.getData() instanceof CommonCommentData) {
                final CommonCommentData data = (CommonCommentData) obj.getData();
                data.setComment(buildComment(data.getComment(), comment));
                return;
            }
            System.out.println("Comment unknown for: " + obj.getData());
            break;
        }
    }

    static void processExamples(final CommentStack commentStack) {
        if (commentStack == null)
            return;
        final StackObj<?> obj = commentStack.peek();
        if (obj == null)
            return;
        final Object data = obj.getData();
        if (data instanceof MethodData) {
            final MethodData dataType = (MethodData) data;
            setStack(commentStack, new StackObj<>(LineTags.EXAMPLES, dataType.getExample()));
        }
    }

    static void processExitCodes(final CommentStack commentStack) {
        if (commentStack == null)
            return;
        final StackObj<?> obj = commentStack.peek();
        if (obj == null)
            return;
        final Object data = obj.getData();
        if (data instanceof MethodData) {
            final MethodData dataType = (MethodData) data;
            setStack(commentStack, new StackObj<>(LineTags.EXIT_CODES, dataType.getExitCode()));
            return;
        }

        if (data instanceof FileData) {
            final FileData dataType = (FileData) data;
            setStack(commentStack, new StackObj<>(LineTags.EXIT_CODES, dataType.getExitCode()));
        }
    }

    private static void processExitCodes(final List<ExitCodeData> data, final String comment) {
        if (comment == null || comment.isBlank())
            return;
        final String[] exitData = comment.split("\\|");
        if (exitData.length < 1)
            return;
        final ExitCodeData output = new ExitCodeData();
        final String number = exitData[0].trim();
        Integer code = null;
        try {
            if (number.isBlank() && exitData.length < 2)
                return;
            if (!number.isBlank())
                code = Integer.parseInt(number);
        } catch (final NumberFormatException e) {
            return;
        }
        data.add(output);
        output.setCode(code);
        if (exitData.length < 2)
            return;
        output.setDescription(exitData[1].trim());
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

    private static void processMethod(final Stack<StackObj<?>> stack, final List<MethodData> data) {
        final MethodData obj = new MethodData();
        data.add(obj);
        setStack(stack, new StackObj<>(LineTags.METHOD, obj));
    }

    private static void processParameters(final List<ParameterData> data, final String comment) {
        if (comment == null || comment.isBlank())
            return;
        final String[] paramData = comment.split("\\|");
        if (paramData.length < 1)
            return;
        final ParameterData output = new ParameterData();
        final String number = paramData[0].replace("$", "").trim();
        Integer position = null;
        try {
            if (number.isBlank() && paramData.length < 2)
                return;
            if (!number.isBlank())
                position = Integer.parseInt(number);
        } catch (final NumberFormatException e) {
            return;
        }
        data.add(output);
        output.setPosition(position);
        if (paramData.length < 2)
            return;
        output.setName(paramData[1].trim());
        if (paramData.length < 3)
            return;
        output.setDescrtiption(paramData[2].trim());
    }

    private static void processParameters(final Stack<StackObj<?>> stack, final String data) {
        final StackObj<?> obj = stack.peek();
        if (obj == null)
            return;
        if (obj.getData() instanceof MethodData) {
            final MethodData methodData = (MethodData) obj.getData();
            setStack(stack, new StackObj<>(LineTags.PARAMETERS, methodData.getParameter()));
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

    static void processReturn(final CommentStack commentStack) {
        if (commentStack == null)
            return;
        final StackObj<?> obj = commentStack.peek();
        if (obj == null)
            return;
        final Object data = obj.getData();
        if (data instanceof MethodData) {
            final MethodData dataType = (MethodData) data;
            setStack(commentStack, new StackObj<>(LineTags.RETURN, dataType));
            return;
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
