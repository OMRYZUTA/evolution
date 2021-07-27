package il.ac.mta.zuli.evolution.engine.xmlparser;

import il.ac.mta.zuli.evolution.engine.data.Descriptor;
import il.ac.mta.zuli.evolution.engine.data.generated.ETTDescriptor;

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

            //creating singleton Descriptor and setting its fields using the ETTDescriptor
            Descriptor newDescriptor = Descriptor.getInstance();
            newDescriptor.setDescriptor(d);

            return newDescriptor;

        } catch (JAXBException e) {
            e.printStackTrace();
            return null;
        }
    }
}
