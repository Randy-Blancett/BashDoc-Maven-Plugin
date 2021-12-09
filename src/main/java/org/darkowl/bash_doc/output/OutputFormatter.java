package org.darkowl.bash_doc.output;

import java.io.IOException;
import java.nio.file.Path;

import org.apache.maven.plugin.logging.Log;
import org.darkowl.bash_doc.model.Library;

public interface OutputFormatter {
    public void process(final Log log, final Path outputDir, final Library library) throws IOException;
}
