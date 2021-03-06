package org.darkowl.bash_doc.builders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.darkowl.bash_doc.builders.FileDataBuilder.CommentStack;
import org.darkowl.bash_doc.builders.FileDataBuilder.StackObj;
import org.darkowl.bash_doc.enums.LineTags;
import org.darkowl.bash_doc.model.ExitCodeData;
import org.darkowl.bash_doc.model.FileData;
import org.darkowl.bash_doc.model.MethodData;
import org.darkowl.bash_doc.model.ParameterData;
import org.darkowl.bash_doc.model.ScopeType;
import org.darkowl.bash_doc.model.VariableData;
import org.darkowl.bash_doc.model.VersionHistoryData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class FileDataBuilderTest {

    @ParameterizedTest
    @CsvSource({
            "   ,       , ''", "' ',       , ''", "   ,' '    , ''", "Old,       , Old", "   ,New    , New",
            "Old,New    , 'Old\nNew'"
    })
    void testBuildComment(final String oldData, final String newData, final String expected) {
        assertEquals(expected, FileDataBuilder.buildComment(oldData, newData));

    }

    @Test
    void testCreate() throws IOException {
        final FileData data = FileDataBuilder.create(Paths.get("target/bash/Test1.sh"));

        assertEquals("Test1.sh", data.getFileName());
        assertEquals("File Comment1\nFile Comment2", data.getComment());
        assertEquals("1.0.0", data.getVersion());
        assertEquals("Randy Blancett", data.getAuthor());
        assertEquals("Randy.Blancett@Gmail.com", data.getAuthorEmail());
        assertEquals(2, data.getVersionHistory().size());
        VersionHistoryData history = data.getVersionHistory().get(0);
        assertEquals(" - Some Feature\n - Some Other Feature", history.getComment());
        assertEquals("1.0.0", history.getVersion());
        assertEquals("25Jan2021", history.getRelease());
        assertEquals("Randy Blancett", history.getAuthor());
        assertEquals("Randy.Blancett@Gmail.com", history.getAuthorEmail());
        history = data.getVersionHistory().get(1);
        assertEquals("0.1.1", history.getVersion());
        assertEquals(" - Things", history.getComment());
        assertEquals("12Dec2021", history.getRelease());

        assertEquals(2, data.getExitCode().size());
        ExitCodeData exitCode = data.getExitCode().get(0);
        assertEquals(0, exitCode.getCode());
        assertEquals("No Errors", exitCode.getDescription());
        exitCode = data.getExitCode().get(1);
        assertEquals(1, exitCode.getCode());
        assertEquals("Some Error", exitCode.getDescription());

        assertEquals(4, data.getVariable().size());
        VariableData variable = data.getVariable().get(0);
        assertEquals(ScopeType.PRIVATE, variable.getScope());
        assertEquals("Private Variable", variable.getComment());
        assertEquals("VAR1", variable.getName());
        assertEquals("Randy Blancett", variable.getAuthor());
        assertEquals("Randy.Blancett@gmail.com", variable.getAuthorEmail());
        assertEquals("1", variable.getDefault());

        variable = data.getVariable().get(1);
        assertEquals(ScopeType.PUBLIC, variable.getScope());
        assertEquals("Public Variable", variable.getComment());
        assertEquals("VAR2", variable.getName());
        assertEquals("2", variable.getDefault());

        variable = data.getVariable().get(2);
        assertEquals(ScopeType.PROTECTED, variable.getScope());
        assertEquals("Protected Variable", variable.getComment());
        assertEquals("VAR3", variable.getName());
        assertNull(variable.getDefault());

        variable = data.getVariable().get(3);
        assertEquals(ScopeType.PROTECTED, variable.getScope());
        assertEquals("Protected Variable", variable.getComment());
        assertEquals("VAR4", variable.getName());
        assertNull(variable.getDefault());

        final MethodData method = data.getMethod().get(0);
        assertEquals("function1", method.getName());
        assertEquals(ScopeType.PUBLIC, method.getScope());
        assertEquals("The Description", method.getComment());
        final List<ParameterData> parameters = method.getParameter();
        assertEquals(2, parameters.size());
        assertEquals(1, parameters.get(0).getPosition());
        assertEquals("Flag", parameters.get(0).getName());
        assertEquals("Flag Desciption", parameters.get(0).getDescrtiption());
        assertEquals(2, parameters.get(1).getPosition());
        assertEquals("Long Command", parameters.get(1).getName());
        assertEquals("Long Command Description", parameters.get(1).getDescrtiption());

        assertEquals(2, method.getExample().size());
        assertEquals("Example 1", method.getExample().get(0));
        assertEquals("Example 2", method.getExample().get(1));

        assertEquals("This will return Hello World.", method.getReturn());

        assertEquals(2, method.getExitCode().size());
        exitCode = method.getExitCode().get(0);
        assertEquals(0, exitCode.getCode());
        assertEquals("Everything worked fine", exitCode.getDescription());
        exitCode = method.getExitCode().get(1);
        assertEquals(1, exitCode.getCode());
        assertEquals("Something went very wrong...", exitCode.getDescription());
    }

    @ParameterizedTest
    @CsvSource({
            ",false", "' ',false", "(),false", "Something,true"
    })
    void testOutputDefaultValue(final String value, final Boolean expected) {
        assertEquals(expected, FileDataBuilder.outputDefaultValue(value));
    }

    @Test
    void testPopStack() {
        Stack<StackObj<?>> stack = null;
        FileDataBuilder.popStack(stack, null);
        assertNull(stack);

        stack = new Stack<>();
        FileDataBuilder.popStack(stack, null);
        assertEquals(0, stack.size());

        stack.add(new StackObj<String>(LineTags.CODE, null));
        stack.add(new StackObj<String>(LineTags.CODE, null));
        stack.add(new StackObj<String>(LineTags.CODE, null));
        stack.add(new StackObj<String>(LineTags.CODE, null));
        stack.add(new StackObj<String>(LineTags.CODE, null));

        assertEquals(5, stack.size());

        FileDataBuilder.popStack(stack, LineTags.AUTHOR);

        assertEquals(LineTags.AUTHOR.getLevel() - 1, stack.size());

    }

    @Test
    void testProcessExamples() {
        final CommentStack stack = new CommentStack();
        FileDataBuilder.processExamples(null);
        stack.push(null);
        FileDataBuilder.processExamples(stack);
    }

    @Test
    void testSetStack() {
        final Stack<StackObj<?>> stack = new Stack<>();
        FileDataBuilder.setStack(stack, new StackObj<>(LineTags.FILE, new FileData()));
        assertEquals(1, stack.size());
        FileDataBuilder.setStack(stack, new StackObj<List<VersionHistoryData>>(LineTags.VERSIONS, new ArrayList<>()));
        assertEquals(2, stack.size());
        FileDataBuilder.setStack(stack, new StackObj<>(LineTags.FILE, new FileData()));
        assertEquals(1, stack.size());
    }

}
