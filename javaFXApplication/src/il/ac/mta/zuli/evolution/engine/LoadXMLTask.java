package il.ac.mta.zuli.evolution.engine;

import il.ac.mta.zuli.evolution.engine.xmlparser.XMLParser;
import javafx.application.Platform;
import javafx.concurrent.Task;

import java.util.function.Consumer;

public class LoadXMLTask extends Task<Boolean> {
    private final String fileName;
    Consumer<Descriptor> descriptorConsumer;
    Consumer<String> fileNameConsumer;

    public LoadXMLTask(String fileToLoad, Consumer<Descriptor> descriptorConsumer, Consumer<String> fileNameConsumer) {
        this.fileName = fileToLoad;
        this.descriptorConsumer = descriptorConsumer;
        this.fileNameConsumer = fileNameConsumer;
    }

    @Override
    protected Boolean call() throws Exception {
        try {
            updateMessage("Fetching file " + fileName);

            XMLParser xmlParser = new XMLParser();
            Descriptor descriptor = xmlParser.unmarshall(fileName);
            descriptorConsumer.accept(descriptor);

            Platform.runLater(
                    () -> fileNameConsumer.accept(fileName)
            );

            updateMessage("File was successfully loaded");

        } catch (Throwable e) {
            e.printStackTrace();
        }

        return Boolean.TRUE;

        //descriptor not ready, meaning invalid file
//            if (UI.this.fileLoaded) {
//                System.out.println("Reverted to last file that was successfully loaded.");
//            } else {
//                System.out.println("There is no file loaded to the system.");
//            }
    }
}
