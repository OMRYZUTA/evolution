package il.ac.mta.zuli.evolution.engine.xmlparser;

import il.ac.mta.zuli.evolution.engine.Descriptor;
import il.ac.mta.zuli.evolution.engine.xmlparser.generated.ex1.ETTDescriptor;
import org.jetbrains.annotations.NotNull;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.UnmarshalException;
import javax.xml.bind.Unmarshaller;
import java.io.File;

public class XMLParser {
    public Descriptor unmarshall(@NotNull String path) throws Throwable {
        try {
            File file = new File(path);

            JAXBContext jaxbContext = JAXBContext.newInstance(ETTDescriptor.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            ETTDescriptor d = (ETTDescriptor) jaxbUnmarshaller.unmarshal(file);

            return new Descriptor(d);
        } catch (UnmarshalException e) {
            throw e.getLinkedException();
        }
    }
}
