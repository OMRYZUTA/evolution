package il.ac.mta.zuli.evolution.ui.mainComponent;

import il.ac.mta.zuli.evolution.engine.Engine;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.Optional;

public class MainTTController {
    @FXML
    Button openFileButton;

    private Engine engine;
    private Stage primaryStage;


    public MainTTController() {
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

    public void onTaskFinished(Optional<Runnable> onFinish) {
        onFinish.ifPresent(Runnable::run);
    }
}
