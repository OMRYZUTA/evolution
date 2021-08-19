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
    Button openFileButton;
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

    private final SimpleStringProperty selectedFileProperty;

    private Engine engine;
    private DescriptorDTO descriptor; //the root in the xml hierarchy
    private Stage primaryStage;
    private final Map<String, RuleController> ruleNameToRuleController;

    public MainTTController() {
        selectedFileProperty = new SimpleStringProperty("");
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
        //TODO change binding of disable from isDescriptorReady to somethingelse
//        displaySettingsButton.disableProperty().bind(isDescriptorReady.not());
//        runEngineButton.disableProperty().bind(isDescriptorReady.not());
//        bestSolutionButton.disableProperty().bind(isDescriptorReady.not());
//        historyButton.disableProperty().bind(isDescriptorReady.not());
    }

    @FXML
    public void openFileButtonAction() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select words file");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("xml files", "*.xml"));
        File selectedFile = fileChooser.showOpenDialog(primaryStage);
        if (selectedFile == null) {
            return;
        }

        String absolutePath = selectedFile.getAbsolutePath();

        SimpleBooleanProperty wasCurrentLoadSuccessful = new SimpleBooleanProperty(false);

        engine.loadXML(absolutePath,
                wasCurrentLoadSuccessful::set,
                selectedFileProperty::set,
                taskMessageLabel.textProperty()::set,
                () -> {
                    toggleTaskButtons(false);
                    if (!wasCurrentLoadSuccessful.get()) {

                        //if(selectedFileProperty)
                    }
                }
        );


        //if File was successfully loaded from - message that the task will convey
//        } else {
        // if selectedFileProperty is an empty string it means a file was never initial loaded
        // if selectedFileProperty isn't empty and we fail to load new file we revert to the file in this property


        //TODO does this happen inside the task?
        //descriptor not ready, meaning invalid file
//            if (UI.this.fileLoaded) {
//                System.out.println("Reverted to last file that was successfully loaded.");
//            } else {
//                System.out.println("There is no file loaded to the system.");
//            }
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
