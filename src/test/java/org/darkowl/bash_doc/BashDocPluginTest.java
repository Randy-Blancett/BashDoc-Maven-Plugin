package org.darkowl.bash_doc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.maven.plugin.MojoExecutionException;
import org.darkowl.bash_doc.model.Library;
import org.junit.jupiter.api.Test;

public class BashDocPluginTest {
    @Test
    public void testExecute() throws Exception {
        final BashDocPlugin plugin = new BashDocPlugin();
        plugin.setSrcDirectory(Paths.get("target/bash"));
        final Path output = Paths.get("target/testOutput");
        plugin.setOutputDirectory(output);
        plugin.execute();
        final Library lib = plugin.getLibrary();
        assertTrue(Files.exists(output));
        assertNotNull(lib.getCreated());
        assertEquals(2, lib.getFiles().size());
        Files.delete(output);
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
        final Path output = Paths.get("target/testOutput");
        plugin.setOutputDirectory(output);
        assertFalse(Files.exists(output));
        plugin.execute();
        assertTrue(Files.exists(output));
        Files.delete(output);
    }
}
