package org.darkowl.bash_doc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;

import org.apache.maven.plugin.MojoExecutionException;
import org.darkowl.bash_doc.model.Library;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class BashDocPluginTest {

    final static Path OUTPUT_DIR = Paths.get("target/testOutput");

    @BeforeAll
    static void setup() throws IOException {
        if (Files.exists(OUTPUT_DIR))
            Files.walk(OUTPUT_DIR).sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);
    }

    @Test
    public void testExecute() throws Exception {
        final BashDocPlugin plugin = new BashDocPlugin();
        plugin.setSrcDirectory(Paths.get("target/bash"));
        plugin.setOutputDirectory(OUTPUT_DIR);
        plugin.execute();
        final Library lib = plugin.getLibrary();
        assertTrue(Files.exists(OUTPUT_DIR));
        assertNotNull(lib.getCreated());
        assertEquals(2, lib.getFiles().size());
        Files.delete(OUTPUT_DIR);
    }

    /**
     * @throws Exception
     *             if any
     */
    @Test
    public void testExecute_badDirectory() throws Exception {
        final BashDocPlugin plugin = new BashDocPlugin();
        boolean hasError = false;
        plugin.setSrcDirectory(Paths.get("no/where.txt"));
        try {
            plugin.execute();
        } catch (final MojoExecutionException e) {
            hasError = true;
            assertTrue(e.getMessage().endsWith(" does not exist"));
        }
        assertTrue(hasError);
        plugin.setSrcDirectory(Paths.get("target/bash"));
        plugin.setOutputDirectory(OUTPUT_DIR);
        assertFalse(Files.exists(OUTPUT_DIR));
        plugin.execute();
        assertTrue(Files.exists(OUTPUT_DIR));
        Files.delete(OUTPUT_DIR);
    }
}
