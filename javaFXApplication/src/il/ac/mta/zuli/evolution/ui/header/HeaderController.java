package il.ac.mta.zuli.evolution.ui.header;

import il.ac.mta.zuli.evolution.dto.DescriptorDTO;
import il.ac.mta.zuli.evolution.dto.TimeTableSolutionDTO;
import il.ac.mta.zuli.evolution.engine.Engine;
import il.ac.mta.zuli.evolution.ui.app.AppController;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class HeaderController {
    @FXML
    private Button displaySettingsButton;

    @FXML
    private Button runEngineButton;

    @FXML
    private Button bestSolutionButton;

    @FXML
    private Button historyButton;

    @FXML
    private Label selectedFileNameLabel;

    @FXML
    private Label taskMessageLabel;

    private final SimpleBooleanProperty fileLoadedProperty;
    private final SimpleBooleanProperty evolutionAlgoCompletedProperty;
    private final SimpleStringProperty selectedFileProperty;
    private AppController appController;
    private Stage primaryStage;
    private Engine engine;
    private DescriptorDTO descriptor; //the root in the xml hierarchy
    private final TimeTableSolutionDTO solution = null;

    public HeaderController() {
        selectedFileProperty = new SimpleStringProperty("");
        fileLoadedProperty = new SimpleBooleanProperty(false);
        evolutionAlgoCompletedProperty = new SimpleBooleanProperty(false);
    }

    public void setAppController(AppController appController) {
        this.appController = appController;
    }

    public void setEngine(Engine engine) {
        this.engine = engine;
        engine.setController(this);
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @FXML
    private void initialize() {
        selectedFileNameLabel.textProperty().bind(selectedFileProperty);
        //disabling 2 of the buttons until a file is loaded to the system
        displaySettingsButton.disableProperty().bind(fileLoadedProperty.not());
        runEngineButton.disableProperty().bind(fileLoadedProperty.not());
        //disabling the remaining buttons until the algorithm completed at least once
        bestSolutionButton.disableProperty().bind(evolutionAlgoCompletedProperty.not());
        historyButton.disableProperty().bind(evolutionAlgoCompletedProperty.not());
    }

    @FXML
    public void loadFileButtonAction() {
//        FileChooser fileChooser = new FileChooser();
//        fileChooser.setTitle("Select file");
//        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("xml files", "*.xml"));
//        File selectedFile = fileChooser.showOpenDialog(primaryStage);
//        if (selectedFile == null) {
//            return;
//        }
//
//        String absolutePath = selectedFile.getAbsolutePath();
        String absolutePath = "javaFXApplication\\src\\resources\\EX2-small.xml";
//        String absolutePath ="C:\\Users\\fifil\\source\\repos\\evolution\\javaFXApplication\\src\\resources\\EX2-error-3.4.xml";

        // engine.loadXML(String fileToLoad,Consumer<DescriptorDTO> onSuccess,Consumer<Throwable> onFailure)
        engine.loadXML(
                absolutePath,
                result -> {
                    selectedFileProperty.set(absolutePath); //so this path will now be displayed (because of the binding)
                    fileLoadedProperty.set(true); // because we're in the onSuccessConsumer
                },
                throwable -> {
                    if (!fileLoadedProperty.get()) {
                        taskMessageLabel.setText("Failed loading the file." + System.lineSeparator()
                                + throwable.getMessage() + System.lineSeparator()
                                + "There is no file loaded to the system.");
                    } else {
                        taskMessageLabel.setText("Failed loading the file." + System.lineSeparator()
                                + throwable.getMessage() + System.lineSeparator()
                                + "Reverted to last file that was successfully loaded.");
                    }
                });
    }

    @FXML
    public void displaySettingsAction() {
        this.appController.displaySettings(engine.getDescriptorDTO());
    }

    @FXML
    public void runEngineAction() {
        if (evolutionAlgoCompletedProperty.get()) {
            //TODO add popup?
            // "The evolution-algorithm has already completed its course. "+ System.lineSeparator() +
            // "If you choose to re-run it, the information from the previous run will be lost." + System.lineSeparator() +
            // "Would you like to re-run the algorithm? (Enter Y/N)");
        }

        this.appController.runAlgorithm();
    }


    @FXML
    public void bestSolutionAction() {
        this.appController.displaySolution(descriptor.getTimeTable());
    }

    @FXML
    public void historyAction() {
    }

    //in the recording about 26 minutes in
    public void bindTaskToUIComponents(Task<?> task) {
        taskMessageLabel.setText("");
        // task message
        taskMessageLabel.textProperty().bind(task.messageProperty());
    }

    // mentions this in the recording about 27 minutes in
    //this function updates the ui once the task is finished
    public void onTaskFinished() {
        this.taskMessageLabel.textProperty().unbind();
    }

    public void onAlgorithmFinished() {
        evolutionAlgoCompletedProperty.set(true);
    }
    //TODO add UIAdaptor and use it in loadXMLTask (better for later use of task in Ex. 3)
    ///uiAdaptor - the way in which the task reports its data back to the UI
    // about 57 minutes into the recording!
}
