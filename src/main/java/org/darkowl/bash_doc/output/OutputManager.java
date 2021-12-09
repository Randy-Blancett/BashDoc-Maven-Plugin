package org.darkowl.bash_doc.output;

import java.nio.file.Path;
import java.util.Map;

import org.apache.maven.plugin.logging.Log;
import org.darkowl.bash_doc.enums.OutputType;
import org.darkowl.bash_doc.model.Library;

public class OutputManager {
    private static Map<OutputType, Boolean> formatSettings = null;

    public static void output(Log log, Path outputDirectory, Library library) {
        formatSettings.forEach((type, value) -> {
            log.debug("Output Setting: " + type + " -> " + value);
            if (type == null || type.getFormatter() == null || value == null || !value)
                return;
            type.getFormatter().process(log, outputDirectory, library);
        });
    }

    public static void init(Map<OutputType, Boolean> settings) {
        formatSettings = settings;
    }

}
