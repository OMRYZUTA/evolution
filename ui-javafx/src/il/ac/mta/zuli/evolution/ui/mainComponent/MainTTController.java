package il.ac.mta.zuli.evolution.ui.mainComponent;

import il.ac.mta.zuli.evolution.engine.Engine;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
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
import java.util.Optional;

public class MainTTController {
    @FXML
    FlowPane histogramFlowPane;
    @FXML
    Label totalLinesLabel;
    @FXML
    Label totalWordsLabel;
    @FXML
    Button collectMetadataButton;
    @FXML
    Button calculateHistogramButton;
    @FXML
    ProgressBar taskProgressBar;
    @FXML
    Label distinctWordsSoFar;
    @FXML
    Label taskMessageLabel;
    @FXML
    Label progressPercentLabel;
    @FXML
    Label selectedFileName;
    @FXML
    Button openFileButton;
    @FXML
    Button clearButton;
    @FXML
    Button clearTaskButton;
    @FXML
    Button stopTaskButton;
    @FXML
    Label totalCurrentProcessedWords;

    private Engine engine;
    private final SimpleLongProperty totalWords;
    private final SimpleLongProperty totalLines;
    private final SimpleIntegerProperty totalDistinctWords;
    private final SimpleIntegerProperty totalProcessedWords;
    private final SimpleStringProperty selectedFileProperty;
    private final SimpleBooleanProperty isFileSelected;
    private final SimpleBooleanProperty isMetadataCollected;
    private Stage primaryStage;


    public MainTTController() {
        totalWords = new SimpleLongProperty(0);
        totalLines = new SimpleLongProperty(0);
        totalDistinctWords = new SimpleIntegerProperty(0);
        totalProcessedWords = new SimpleIntegerProperty(0);
        selectedFileProperty = new SimpleStringProperty();
        isFileSelected = new SimpleBooleanProperty(false);
        isMetadataCollected = new SimpleBooleanProperty(false);
    }

    public SimpleStringProperty fileNameProperty() {
        return this.fileName;
    }

    public void setEngine(Engine newEngine) {
        this.engine = newEngine;
        newEngine.fileNameProperty().bind(selectedFileProperty);
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @FXML
    private void initialize() {
        totalWordsLabel.textProperty().bind(Bindings.format("%,d", totalWords));
        totalLinesLabel.textProperty().bind(Bindings.format("%,d", totalLines));
        distinctWordsSoFar.textProperty().bind(Bindings.format("%,d", totalDistinctWords));
        totalCurrentProcessedWords.textProperty().bind(Bindings.format("%,d", totalProcessedWords));
        selectedFileName.textProperty().bind(selectedFileProperty);
        collectMetadataButton.disableProperty().bind(isFileSelected.not());
        calculateHistogramButton.disableProperty().bind(isMetadataCollected.not());
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

        String absolutePath = selectedFile.getAbsolutePath();
        selectedFileProperty.set(absolutePath);
        isFileSelected.set(true);
    }

    public void bindTaskToUIComponents(Task<Boolean> aTask, Runnable onFinish) {
        // task message
        taskMessageLabel.textProperty().bind(aTask.messageProperty());

        // task progress bar
        taskProgressBar.progressProperty().bind(aTask.progressProperty());

        // task percent label
        progressPercentLabel.textProperty().bind(
                Bindings.concat(
                        Bindings.format(
                                "%.0f",
                                Bindings.multiply(
                                        aTask.progressProperty(),
                                        100)),
                        " %"));

        // task cleanup upon finish
        aTask.valueProperty().addListener((observable, oldValue, newValue) -> {
            onTaskFinished(Optional.ofNullable(onFinish));
        });
    }

    public void onTaskFinished(Optional<Runnable> onFinish) {
        this.taskMessageLabel.textProperty().unbind();
        this.progressPercentLabel.textProperty().unbind();
        this.taskProgressBar.progressProperty().unbind();
        onFinish.ifPresent(Runnable::run);
    }

//    private UIAdapter createUIAdapter() {
//        return new UIAdapter(
//                histogramData -> {
//                    HistogramsUtils.log("EDT: CREATE new tile for [" + histogramData.toString() + "]");
//                    createTile(histogramData.getWord(), histogramData.getCount());
//                },
//                histogramData -> {
//                    HistogramsUtils.log("EDT: UPDATE tile for [" + histogramData.toString() + "]");
//                    SingleWordController singleWordController = wordToTileController.get(histogramData.getWord());
//                    if (singleWordController != null && singleWordController.getCount() != histogramData.getCount()) {
//                        singleWordController.setCount(histogramData.getCount());
//                    } else {
//                        HistogramsUtils.log("ERROR ! Can't find tile for [" + histogramData.getWord() + "] with count " + histogramData.getCount());
//                    }
//                },
//                () -> {
//                    HistogramsUtils.log("EDT: INCREASE total distinct words");
//                    totalDistinctWords.set(totalDistinctWords.get() + 1);
//                },
//                (delta) -> {
//                    HistogramsUtils.log("EDT: INCREASE total processed words");
//                    totalProcessedWords.set(totalProcessedWords.get() + delta);
//                }
//        );
//    }
}
