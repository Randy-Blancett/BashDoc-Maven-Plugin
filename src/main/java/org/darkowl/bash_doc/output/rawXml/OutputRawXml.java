package org.darkowl.bash_doc.output.rawXml;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.maven.plugin.logging.Log;
import org.darkowl.bash_doc.model.Library;
import org.darkowl.bash_doc.output.OutputFormatter;

public class OutputRawXml implements OutputFormatter {

    @Override
    public void process(Log log, Path outputDir, Library library) {
        try {
            final JAXBContext context = JAXBContext.newInstance(Library.class);
            final Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            final Path rawXmlDir = outputDir.resolve("xml");
            final Path rawXmlFile = rawXmlDir.resolve("RawXml.xml");
            if (log != null)
                log.info("Writing Raw Xml to: " + rawXmlFile.toAbsolutePath());
            Files.createDirectories(rawXmlDir);
            marshaller.marshal(library, Files.newOutputStream(rawXmlFile, StandardOpenOption.CREATE));
        } catch (JAXBException | IOException e) {
            if (log != null)
                log.error("Failed to output Raw Xml.", e);
        }
    }

}
