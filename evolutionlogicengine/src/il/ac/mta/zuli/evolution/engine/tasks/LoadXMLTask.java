package il.ac.mta.zuli.evolution.engine.tasks;

import il.ac.mta.zuli.evolution.engine.Descriptor;
import il.ac.mta.zuli.evolution.engine.xmlparser.XMLParser;
import javafx.concurrent.Task;

import java.io.InputStream;


public class LoadXMLTask extends Task<Descriptor> {
    private String fileName;
    private InputStream inputStream;

    //for Ex 3:
    public LoadXMLTask(InputStream is) {
        this.inputStream = is;
    }

    public LoadXMLTask(String fileToLoad) {
        this.fileName = fileToLoad;
    }


    @Override
    protected Descriptor call() throws Exception {
        updateMessage("Fetching file " + fileName);

        XMLParser xmlParser = new XMLParser();
//        Descriptor descriptor = xmlParser.unmarshall(fileName);
        Descriptor descriptor = xmlParser.unmarshall(inputStream); // for Ex: 3

        updateMessage("File was successfully loaded");

        return descriptor;
    }
}
