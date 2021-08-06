package org.darkowl.bash_doc.builders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.darkowl.bash_doc.builders.FileDataBuilder.StackObj;
import org.darkowl.bash_doc.enums.LineTags;
import org.darkowl.bash_doc.model.FileData;
import org.darkowl.bash_doc.model.ScopeType;
import org.darkowl.bash_doc.model.VariableData;
import org.darkowl.bash_doc.model.VersionHistoryData;
import org.junit.jupiter.api.Test;

class FileDataBuilderTest {

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

        assertEquals(3, data.getVariable().size());
        VariableData variable = data.getVariable().get(0);
        assertEquals(ScopeType.PRIVATE, variable.getScope());
        assertEquals("Private Variable", variable.getComment());
        assertEquals("VAR1", variable.getName());
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
    }

    @Test
    void testSetStack() {
        final Stack<StackObj<?>> stack = new Stack<>();
        FileDataBuilder.setStack(stack, new StackObj<>(LineTags.FILE, new FileData()));
        assertEquals(1, stack.size());
        FileDataBuilder.setStack(stack, new StackObj<List<VersionHistoryData>>(LineTags.VERSIONS, new ArrayList<>()));
        assertEquals(2, stack.size());
        // FileDataBuilder.setStack(stack, LineTags.HISTORIC_VERSION, new FileData());
        // assertEquals(3, stack.size());
        FileDataBuilder.setStack(stack, new StackObj<>(LineTags.FILE, new FileData()));
        assertEquals(1, stack.size());
    }

}
