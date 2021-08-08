package org.darkowl.bash_doc.output.text;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.darkowl.bash_doc.model.ScopeType;
import org.darkowl.bash_doc.model.VariableData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class VariableTextSortTest {
    private VariableTextSort sorter = new VariableTextSort();

    @ParameterizedTest
    @CsvSource({ "aaa, PUBLIC,   aaa, PRIVATE,   true", "aaa, PRIVATE,  aaa, PUBLIC,    false",
            "aaa, PRIVATE,  aaa, ,          true", "aaa, ,         aaa, PUBLIC,    false",
            "aaa, PROTECTED,aaa, PUBLIC,    false", "aaa, PUBLIC,   aaa, PROTECTED, true",
            "aaa, PROTECTED,aaa, PRIVATE,   true", "aaa, PRIVATE,  aaa, PROTECTED, false",
            "aaa, PUBLIC,   aab, PUBLIC,    true", "aac, PUBLIC,   aab, PUBLIC,    false",
            ",    PUBLIC,   aab, PUBLIC,    false", "aac, PUBLIC,   ,    PUBLIC,    true" })
    void testCompare(String arg1Name, ScopeType arg1Scope, String arg2Name, ScopeType arg2Scope, boolean firstFirst) {
        VariableData arg1 = new VariableData();
        arg1.setName(arg1Name);
        arg1.setScope(arg1Scope);

        VariableData arg2 = new VariableData();
        arg2.setName(arg2Name);
        arg2.setScope(arg2Scope);

        List<VariableData> l1 = new ArrayList<>();
        l1.add(arg1);
        l1.add(arg2);

        List<VariableData> l2 = new ArrayList<>();
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
