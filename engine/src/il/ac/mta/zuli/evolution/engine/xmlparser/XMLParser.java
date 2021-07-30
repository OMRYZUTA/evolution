package il.ac.mta.zuli.evolution.engine.xmlparser;

import il.ac.mta.zuli.evolution.engine.Descriptor;
import il.ac.mta.zuli.evolution.engine.xmlparser.generated.ETTDescriptor;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;

public class XMLParser {
    public Descriptor unmarshall(String path) {
        try {
            File file = new File(path);

            JAXBContext jaxbContext = JAXBContext.newInstance(ETTDescriptor.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            ETTDescriptor d = (ETTDescriptor) jaxbUnmarshaller.unmarshal(file);

            return new Descriptor(d);

        } catch (JAXBException e) {
            e.printStackTrace();
            return null;
        }
    }
}
