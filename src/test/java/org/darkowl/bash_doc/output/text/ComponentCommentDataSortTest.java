package org.darkowl.bash_doc.output.text;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.darkowl.bash_doc.model.ComponentCommentData;
import org.darkowl.bash_doc.model.MethodData;
import org.darkowl.bash_doc.model.ScopeType;
import org.darkowl.bash_doc.model.VariableData;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class ComponentCommentDataSortTest {

    private final ComponentCommentDataSort sorter = new ComponentCommentDataSort();

    @ParameterizedTest
    @CsvSource({
            "aaa, PUBLIC,   aaa, PRIVATE,   true", "aaa, PRIVATE,  aaa, PUBLIC,    false",
            "aaa, PRIVATE,  aaa, ,          true", "aaa, ,         aaa, PUBLIC,    false",
            "aaa, PROTECTED,aaa, PUBLIC,    false", "aaa, PUBLIC,   aaa, PROTECTED, true",
            "aaa, PROTECTED,aaa, PRIVATE,   true", "aaa, PRIVATE,  aaa, PROTECTED, false",
            "aaa, PUBLIC,   aab, PUBLIC,    true", "aac, PUBLIC,   aab, PUBLIC,    false",
            ",    PUBLIC,   aab, PUBLIC,    false", "aac, PUBLIC,   ,    PUBLIC,    true"
    })
    void testCompare(final String arg1Name,
            final ScopeType arg1Scope,
            final String arg2Name,
            final ScopeType arg2Scope,
            final boolean firstFirst) {
        final VariableData arg1 = new VariableData();
        arg1.setName(arg1Name);
        arg1.setScope(arg1Scope);

        final VariableData arg2 = new VariableData();
        arg2.setName(arg2Name);
        arg2.setScope(arg2Scope);

        final List<ComponentCommentData> l1 = new ArrayList<>();
        l1.add(arg1);
        l1.add(arg2);

        final List<ComponentCommentData> l2 = new ArrayList<>();
        l2.add(arg2);
        l2.add(arg1);

        Collections.sort(l1, sorter);
        Collections.sort(l2, sorter);

        if (firstFirst) {
            assertEquals(arg1, l1.get(0));
            assertEquals(arg1, l2.get(0));
        } else {
            assertEquals(arg1, l1.get(1));
            assertEquals(arg1, l2.get(1));
        }
    }

    @ParameterizedTest
    @CsvSource({
            "aaa, PUBLIC,   aaa, PRIVATE,   true", "aaa, PRIVATE,  aaa, PUBLIC,    false",
            "aaa, PRIVATE,  aaa, ,          true", "aaa, ,         aaa, PUBLIC,    false",
            "aaa, PROTECTED,aaa, PUBLIC,    false", "aaa, PUBLIC,   aaa, PROTECTED, true",
            "aaa, PROTECTED,aaa, PRIVATE,   true", "aaa, PRIVATE,  aaa, PROTECTED, false",
            "aaa, PUBLIC,   aab, PUBLIC,    true", "aac, PUBLIC,   aab, PUBLIC,    false",
            ",    PUBLIC,   aab, PUBLIC,    false", "aac, PUBLIC,   ,    PUBLIC,    true"
    })
    void testMethodDataCompare(final String arg1Name,
            final ScopeType arg1Scope,
            final String arg2Name,
            final ScopeType arg2Scope,
            final boolean firstFirst) {
        final MethodData arg1 = new MethodData();
        arg1.setName(arg1Name);
        arg1.setScope(arg1Scope);

        final MethodData arg2 = new MethodData();
        arg2.setName(arg2Name);
        arg2.setScope(arg2Scope);

        final List<ComponentCommentData> l1 = new ArrayList<>();
        l1.add(arg1);
        l1.add(arg2);

        final List<ComponentCommentData> l2 = new ArrayList<>();
        l2.add(arg2);
        l2.add(arg1);

        Collections.sort(l1, sorter);
        Collections.sort(l2, sorter);

        if (firstFirst) {
            assertEquals(arg1, l1.get(0));
            assertEquals(arg1, l2.get(0));
        } else {
            assertEquals(arg1, l1.get(1));
            assertEquals(arg1, l2.get(1));
        }
    }
}
