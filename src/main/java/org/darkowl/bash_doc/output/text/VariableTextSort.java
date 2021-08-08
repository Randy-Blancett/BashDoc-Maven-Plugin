package org.darkowl.bash_doc.output.text;

import java.util.Comparator;
import java.util.Objects;

import org.darkowl.bash_doc.model.ScopeType;
import org.darkowl.bash_doc.model.VariableData;

public class VariableTextSort implements Comparator<VariableData> {

    @Override
    public int compare(final VariableData arg0, final VariableData arg1) {
        if (arg0 == null && arg1 == null)
            return 0;
        if (arg0 == null)
            return 1;
        if (arg1 == null)
            return -1;
        final ScopeType arg0Scope = arg0.getScope();
        final ScopeType arg1Scope = arg1.getScope();
        if (Objects.equals(arg0Scope, arg1Scope)) {
            final String arg0Name = arg0.getName();
            final String arg1Name = arg1.getName();
            if (arg0Name == null)
                return 1;
            if (arg1Name == null)
                return -1;
            return arg0Name.compareTo(arg1Name);
        }
        if (arg0Scope == null)
            return 1;
        if (arg1Scope == null)
            return -1;
        return arg0Scope.compareTo(arg1Scope);
    }

}
