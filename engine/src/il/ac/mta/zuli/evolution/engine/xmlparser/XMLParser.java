package il.ac.mta.zuli.evolution.engine.xmlparser;

import il.ac.mta.zuli.evolution.engine.data.Descriptor;
import il.ac.mta.zuli.evolution.engine.data.generated.ETTDescriptor;
import il.ac.mta.zuli.evolution.engine.data.generated.ETTTeacher;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;

public class XMLParser {
    public Descriptor unmarshall() {
        try {
            //! validate - file ends with xml - delete later
            String path = "engine/src/resources/EX1-small.xml"; // modify later
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

    public void marshall2() {
        try {
            String path = "engine\\src\\resources\\teacher.xml"; // modify later
            File file = new File(path);

            JAXBContext jaxbContext = JAXBContext.newInstance(ETTTeacher.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            ETTTeacher ettTeacher = (ETTTeacher) jaxbUnmarshaller.unmarshal(file);
            System.out.println(ettTeacher);
            //loadedData = ettTeacher;

        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }
}
