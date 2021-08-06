package org.darkowl.bash_doc;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Date;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.darkowl.bash_doc.builders.FileDataBuilder;
import org.darkowl.bash_doc.model.Library;
import org.darkowl.bash_doc.output.text.BashDocTextOutput;

@Mojo(name = "document", defaultPhase = LifecyclePhase.PACKAGE)
public class BashDocPlugin extends AbstractMojo {
    private final Library library = new Library();
    Path outputDirectory = null;
    /**
     * Location of the file.
     */
    @Parameter(defaultValue = "${project.build.directory}/doc", property = "outputDir", required = true)
    private String outputDirectoryStr;
    @Parameter(defaultValue = "true", property = "outputRawXml", required = true)
    private boolean outputRawXml;
    @Parameter(defaultValue = "true", property = "outputText", required = true)
    private boolean outputText;

    Path srcDirectory = null;
    @Parameter(defaultValue = "${project.build.directory}/bash", property = "sourceDir", required = true)
    private String srcDirectoryStr;

    @Override
    public void execute() throws MojoExecutionException {
        if (srcDirectory == null && srcDirectoryStr != null)
            srcDirectory = Paths.get(srcDirectoryStr);
        if (outputDirectory == null && outputDirectoryStr != null)
            outputDirectory = Paths.get(outputDirectoryStr);
        getLog().debug("Source Directory: " + srcDirectory);
        getLog().debug("Output Directory: " + outputDirectory);
        if (srcDirectory == null || !Files.exists(srcDirectory)) {
            getLog().error("Source Directory [" + srcDirectory.toAbsolutePath() + "] does not exist.");
            throw new MojoExecutionException(srcDirectory.toAbsolutePath() + " does not exist");
        }
        if (!Files.isDirectory(srcDirectory)) {
            getLog().error("Source Directory [" + srcDirectory.toAbsolutePath() + "] is not a directory.");
            throw new MojoExecutionException(srcDirectory.toAbsolutePath() + " is not a directory.");
        }

        if (outputDirectory == null || !Files.exists(outputDirectory)) {
            getLog().debug("Creating Output Directory [" + outputDirectory + "].");
            try {
                Files.createDirectories(outputDirectory);
            } catch (final IOException e) {
                getLog().error("Failed to create [" + outputDirectory.toAbsolutePath() + "].");
                throw new MojoExecutionException("Failed to create [" + outputDirectory.toAbsolutePath() + "].", e);
            }
        }
        library.setCreated(new Date());
        try {
            processDirectory(srcDirectory);
        } catch (final IOException e) {
            getLog().error("Failed to read source files from [" + srcDirectory.toAbsolutePath() + "].");
            throw new MojoExecutionException(
                    "Failed to read source files from [" + srcDirectory.toAbsolutePath() + "].", e);
        }
        if (outputRawXml)
            outputRawXml(library);
        if (outputText)
            BashDocTextOutput.output(getLog(), outputDirectory, library);
    }

    public Library getLibrary() {
        return library;
    }

    private void outputRawXml(final Library rawXml) {
        try {
            final JAXBContext context = JAXBContext.newInstance(Library.class);
            final Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            final Path rawXmlDir = outputDirectory.resolve("xml");
            final Path rawXmlFile = rawXmlDir.resolve("RawXml.xml");
            getLog().info("Writing Raw Xml to: " + rawXmlFile.toAbsolutePath());
            Files.createDirectories(rawXmlDir);
            marshaller.marshal(rawXml, Files.newOutputStream(rawXmlFile, StandardOpenOption.CREATE));
        } catch (JAXBException | IOException e) {
            getLog().error("Failed to output Raw Xml.", e);
        }

    }

    public void processDirectory(final Path input) throws IOException {
        getLog().debug("Processing Directory: " + input);
        if (Files.isDirectory(input))
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(input)) {
                stream.forEach(path -> {
                    if (Files.isDirectory(path))
                        try {
                            processDirectory(path);
                        } catch (final IOException e) {
                            getLog().error("Failed to create [" + outputDirectory.toAbsolutePath() + "].", e);
                        }
                    else
                        try {
                            processFile(path);
                        } catch (final IOException e) {
                            getLog().error("Failed Parsing [" + outputDirectory.toAbsolutePath() + "].", e);
                        }
                });
            }
    }

    public void processFile(final Path file) throws IOException {
        library.getFiles().add(FileDataBuilder.create(file));
    }

    public void setOutputDirectory(final Path outputDirectory) {
        this.outputDirectory = outputDirectory;
    }

    public void setSrcDirectory(final Path srcDirectory) {
        this.srcDirectory = srcDirectory;
    }
}
