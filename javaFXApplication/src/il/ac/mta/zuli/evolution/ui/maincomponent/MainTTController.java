package il.ac.mta.zuli.evolution.ui.maincomponent;

import il.ac.mta.zuli.evolution.engine.Engine;
import il.ac.mta.zuli.evolution.ui.rulecomponent.RuleController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MainTTController {
    @FXML
    Button openFileButton;
    @FXML
    FlowPane ruleFlowPane;

    private Engine engine;
    private Stage primaryStage;
    private Map<String, RuleController> ruleNameToRuleController;

    public MainTTController() {
        ruleNameToRuleController= new HashMap<>();
    }

    public void setEngine(Engine newEngine) {
        this.engine = newEngine;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @FXML
    private void initialize() {
    }

    @FXML
    public void openFileButtonAction() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select words file");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("text files", "*.txt"));
        File selectedFile = fileChooser.showOpenDialog(primaryStage);
        if (selectedFile == null) {
            return;
        }

//        String absolutePath = selectedFile.getAbsolutePath();
//        selectedFileProperty.set(absolutePath);
//        isFileSelected.set(true);
    }


//    public void bindTaskToUIComponents(Task<Boolean> aTask, Runnable onFinish) {
//        // task message
//        taskMessageLabel.textProperty().bind(aTask.messageProperty());
//
//        // task progress bar
//        taskProgressBar.progressProperty().bind(aTask.progressProperty());
//
//        // task percent label
//        progressPercentLabel.textProperty().bind(
//                Bindings.concat(
//                        Bindings.format(
//                                "%.0f",
//                                Bindings.multiply(
//                                        aTask.progressProperty(),
//                                        100)),
//                        " %"));
//
//        // task cleanup upon finish
//        aTask.valueProperty().addListener((observable, oldValue, newValue) -> {
//            onTaskFinished(Optional.ofNullable(onFinish));
//        });
//    }


    private void createRuleTile(String ruleName,  String ruleType) {
        try {
            FXMLLoader loader = new FXMLLoader();
            URL ruleFXML = getClass().getResource("il/ac/mta/zuli/evolution/ui/rulecomponent/ruleScene.fxml");
            loader.setLocation(ruleFXML);
            Node singleRuleTile = loader.load();

            RuleController ruleController = loader.getController();
            ruleController.setCount(count);
            singleWordController.setWord(word);

            histogramFlowPane.getChildren().add(singleWordTile);
            wordToTileController.put(word, singleWordController);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onTaskFinished(Optional<Runnable> onFinish) {
        onFinish.ifPresent(Runnable::run);
    }
}
