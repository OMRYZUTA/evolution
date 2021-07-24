package il.ac.mta.zuli.evolution.engine.xmlparser;

import il.ac.mta.zuli.evolution.engine.data.Teacher;
import il.ac.mta.zuli.evolution.engine.data.generated.ETTTeacher;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class XMLParser {
    private final String FILE_NAME = "teacher.xml";
    private ETTTeacher loadedData;

    public void marshall() {
        try {
            String path = "engine\\src\\resources\\teacher.xml";
            File file = new File(path);
            JAXBContext jaxbContext = JAXBContext.newInstance(ETTTeacher.class);

            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            ETTTeacher ettTeacher = (ETTTeacher) jaxbUnmarshaller.unmarshal(file);
            System.out.println(ettTeacher);
            loadedData=ettTeacher;

        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    public Teacher createTeacher() {
      return new Teacher(loadedData);
    }
}
