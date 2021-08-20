package il.ac.mta.zuli.evolution.engine.tasks;

import il.ac.mta.zuli.evolution.engine.Descriptor;
import il.ac.mta.zuli.evolution.engine.xmlparser.XMLParser;
import javafx.concurrent.Task;

//TODO move tasks to ui

public class LoadXMLTask extends Task<Descriptor> {
    private final String fileName;

    public LoadXMLTask(String fileToLoad) {
        this.fileName = fileToLoad;
    }

    @Override
    protected Descriptor call() throws Exception {
        updateMessage("Fetching file " + fileName);

        XMLParser xmlParser = new XMLParser();
        Descriptor descriptor = xmlParser.unmarshall(fileName);

        updateMessage("File was successfully loaded");

        return descriptor;
    }
}
