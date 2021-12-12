package org.darkowl.bash_doc.output;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;

import org.apache.maven.plugin.logging.Log;
import org.darkowl.bash_doc.enums.OutputType;
import org.darkowl.bash_doc.model.Library;

public class OutputManager {
    private static Map<OutputType, Boolean> formatSettings = null;

    public static void init(final Map<OutputType, Boolean> settings) {
        formatSettings = settings;
    }

    public static boolean isOutput(final OutputType type) {
        if (type == null || formatSettings == null)
            return false;
        return formatSettings.getOrDefault(type, false);
    }

    public static void output(final Log log, final Path outputDirectory, final Library library) {
        formatSettings.forEach((type, value) -> {
            log.debug("Output Setting: " + type + " -> " + value);
            if (!isOutput(type))
                return;
            try {
                type.getFormatter().process(log, outputDirectory, library);
            } catch (final IOException e) {
                log.error("Failed to create output for " + type + ".", e);
            }
        });
    }

    private OutputManager() {
    }

}
