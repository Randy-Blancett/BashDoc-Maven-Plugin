package org.darkowl.bash_doc.output;

import java.util.List;

import org.darkowl.bash_doc.model.MethodData;
import org.darkowl.bash_doc.model.ParameterData;

public class ParameterDataProcessor extends BaseDataProcessor<ParameterData> {
    private static ParameterDataProcessor instance = null;

    public static void process(final OutputFormatter outputFormatter,
            final StringBuilder sb,
            final int i,
            final List<ParameterData> params) {
        if (instance == null)
            instance = new ParameterDataProcessor();
        instance.format(outputFormatter, sb, i, params);
    }

    @Override
    protected boolean isValid(ParameterData data) {
        return data.getPosition() != null || data.getName() != null || data.getDescrtiption() != null;
    }

    @Override
    protected void formatData(OutputFormatter outputFormatter, StringBuilder output, int index, ParameterData param) {
        output.append(
                outputFormatter
                        .createParameterOutput(index, param.getPosition(), param.getName(), param.getDescrtiption()));
    }

    @Override
    protected String getHeaderString() {
        return "Parameters";
    }

}
