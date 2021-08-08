package il.ac.mta.zuli.evolution.engine.xmlparser;

import il.ac.mta.zuli.evolution.engine.Descriptor;
import il.ac.mta.zuli.evolution.engine.exceptions.EmptyCollectionException;
import il.ac.mta.zuli.evolution.engine.exceptions.ValidationException;
import il.ac.mta.zuli.evolution.engine.xmlparser.generated.ETTDescriptor;
import org.jetbrains.annotations.NotNull;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.File;

public class XMLParser {
    public Descriptor unmarshall(@NotNull String path) throws Exception {
        try {
            File file = new File(path);

            JAXBContext jaxbContext = JAXBContext.newInstance(ETTDescriptor.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            ETTDescriptor d = (ETTDescriptor) jaxbUnmarshaller.unmarshal(file);

            return new Descriptor(d);
        }catch (ValidationException e){
            throw  e;
        }
        catch (EmptyCollectionException e){
            throw e;
        }
        catch (Exception e){
            throw new Exception("file not loaded",e);
        }
    }
}
