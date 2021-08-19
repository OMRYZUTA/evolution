package il.ac.mta.zuli.evolution.ui.maincomponent;

import il.ac.mta.zuli.evolution.dto.DescriptorDTO;
import il.ac.mta.zuli.evolution.engine.Engine;
import il.ac.mta.zuli.evolution.ui.rulecomponent.RuleController;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.FlowPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MainTTController {
    @FXML
    Button loadFileButton;
    @FXML
    Label selectedFileName;
    @FXML
    Button displaySettingsButton;
    @FXML
    Button runEngineButton;
    @FXML
    Button bestSolutionButton;
    @FXML
    Button historyButton;
    @FXML
    Label taskMessageLabel;
    @FXML
    ProgressBar taskProgressBar;
    @FXML
    Button pauseResumeButton;
    @FXML
    Button stopTaskButton;
    @FXML
    FlowPane ruleFlowPane;

    private Engine engine;
    private DescriptorDTO descriptor; //the root in the xml hierarchy
    private Stage primaryStage;
    private final SimpleStringProperty selectedFileProperty;
    private final SimpleBooleanProperty fileLoaded;
    private final SimpleBooleanProperty evolutionAlgorithmCompleted;

    private final Map<String, RuleController> ruleNameToRuleController;

    public MainTTController() {
        selectedFileProperty = new SimpleStringProperty("");
        fileLoaded = new SimpleBooleanProperty(false);
        evolutionAlgorithmCompleted = new SimpleBooleanProperty(false);
        ruleNameToRuleController = new HashMap<>();
    }

    public void setEngine(Engine newEngine) {
        this.engine = newEngine;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @FXML
    private void initialize() {
        selectedFileName.textProperty().bind(selectedFileProperty);
        //disabling 2 of the buttons until a file is loaded to the system
        displaySettingsButton.disableProperty().bind(fileLoaded.not());
        runEngineButton.disableProperty().bind(fileLoaded.not());
        //disabling the remaining buttons until the algorithm completed at least once
        bestSolutionButton.disableProperty().bind(evolutionAlgorithmCompleted.not());
        historyButton.disableProperty().bind(evolutionAlgorithmCompleted.not());
    }

    @FXML
    public void loadFileButtonAction() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select words file");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("xml files", "*.xml"));
        File selectedFile = fileChooser.showOpenDialog(primaryStage);
        if (selectedFile == null) {
            return;
        }

        String absolutePath = selectedFile.getAbsolutePath();

        //loadXML(String fileToLoad,Consumer<DescriptorDTO> onSuccess,Consumer<Throwable> onFailure)
        engine.loadXML(
                absolutePath,
                descriptorDTO -> {
                    descriptor = descriptorDTO;
                    selectedFileProperty.set(absolutePath); //so this path will now be displayed (because of the binding)
                    fileLoaded.set(true); // because we're in the onSuccessConsumer
                },
                throwable -> {
                    if (descriptor == null) {
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
    }

    @FXML
    public void bestSolutionAction() {
    }

    @FXML
    public void runEngineAction() {
    }

    @FXML
    public void historyAction() {
    }

    //in the recording ~20min. this is the "old-fashioned" way, better to do it with property
    //TODO change to property implementation - no need for method
    private void toggleTaskButtons(boolean isActive) {
        pauseResumeButton.setDisable(!isActive);
        stopTaskButton.setDisable(!isActive);
    }

    //in the recording about 26 minutes in
    public void bindTaskToUIComponents(Task<?> task, Runnable onFinish) {
//done here?
//        taskMessageLabel.setText("");
//        taskProgressBar.setProgress(0);
//
        // task message
        taskMessageLabel.textProperty().bind(task.messageProperty());

        // task progress bar
        taskProgressBar.progressProperty().bind(task.progressProperty());

        // TODO: bind to task.exceptionProperty() maybe?

        // task percent label
//        progressPercentLabel.textProperty().bind(
//                Bindings.concat(
//                        Bindings.format(
//                                "%.0f",
//                                Bindings.multiply(
//                                        task.progressProperty(),
//                                        100)),
//                        " %"));

        // task cleanup upon finish
        task.valueProperty().addListener((observable, oldValue, newValue) -> {
            onTaskFinished(Optional.ofNullable(onFinish));
        });
    }

    // mentions this in the recording about 27 minutes in
    //this function updates the ui once the task is finished
    public void onTaskFinished(Optional<Runnable> onFinish) {
        this.taskMessageLabel.textProperty().unbind();
        //this.progressPercentLabel.textProperty().unbind();
        this.taskProgressBar.progressProperty().unbind();
        onFinish.ifPresent(Runnable::run);
    }

//TODO add UIAdaptor and use it in loadXMLTask (better for later use of task in Ex. 3)
    ///uiAdaptor - the way in which the task reports its data back to the UI
    // about 57 minutes into the recording!

//    private void createRuleTile(String ruleName,  String ruleType) {
//        try {
//            FXMLLoader loader = new FXMLLoader();
//            URL ruleFXML = getClass().getResource("il/ac/mta/zuli/evolution/ui/rulecomponent/ruleScene.fxml");
//            loader.setLocation(ruleFXML);
//            Node singleRuleTile = loader.load();
//
//            RuleController ruleController = loader.getController();
//            ruleController.setCount(count);
//            singleWordController.setWord(word);
//
//            histogramFlowPane.getChildren().add(singleWordTile);
//            wordToTileController.put(word, singleWordController);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

}
