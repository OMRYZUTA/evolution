package il.ac.mta.zuli.evolution.engine.xmlparser;

import il.ac.mta.zuli.evolution.engine.Descriptor;
import il.ac.mta.zuli.evolution.engine.xmlparser.generated.ex2.ETTDescriptor;
import org.jetbrains.annotations.NotNull;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.InputStream;

public class XMLParser {
    public Descriptor unmarshall(@NotNull String path) throws JAXBException {
//        try {
        File file = new File(path);

        JAXBContext jaxbContext = JAXBContext.newInstance(ETTDescriptor.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        ETTDescriptor d = (ETTDescriptor) jaxbUnmarshaller.unmarshal(file);

        return new Descriptor(d);
//        } catch (UnmarshalException e) {
//            throw e.getLinkedException();
//        }
    }

    //for Ex3:
    public Descriptor unmarshall2(@NotNull InputStream is) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(ETTDescriptor.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        //TODO change file to generate descriptor with only timetable in CTOR
        ETTDescriptor d = (ETTDescriptor) jaxbUnmarshaller.unmarshal(is);

        return new Descriptor(d);
    }
}
