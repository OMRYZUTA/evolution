package il.ac.mta.zuli.evolution.ui.header;

import il.ac.mta.zuli.evolution.dto.DescriptorDTO;
import il.ac.mta.zuli.evolution.dto.StrideDataDTO;
import il.ac.mta.zuli.evolution.dto.TimeTableSolutionDTO;
import il.ac.mta.zuli.evolution.engine.Engine;
import il.ac.mta.zuli.evolution.engine.predicates.FinishPredicate;
import il.ac.mta.zuli.evolution.engine.predicates.PredicateType;
import il.ac.mta.zuli.evolution.ui.app.AppController;
import il.ac.mta.zuli.evolution.ui.endConditions.EndConditionsController;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HeaderController {// in our case T is integer or a double, used for predicate
    @FXML
    Button loadFileButton;
    @FXML
    Label selectedFileName; //TODO add "Label" to end of name
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

    private final SimpleBooleanProperty fileLoaded;
    private final SimpleBooleanProperty evolutionAlgorithmCompleted;
    private final SimpleStringProperty selectedFileProperty;
    private SimpleBooleanProperty isPaused;
    private final SimpleBooleanProperty runningAlgorithm;
    private AppController appController;
    private Stage primaryStage;
    private Engine engine;
    private DescriptorDTO descriptor; //the root in the xml hierarchy
    private TimeTableSolutionDTO solution = null;
    private int stride;
    private List<FinishPredicate> finishPredicates;

    public HeaderController() {
        selectedFileProperty = new SimpleStringProperty("");
        fileLoaded = new SimpleBooleanProperty(false);
        evolutionAlgorithmCompleted = new SimpleBooleanProperty(false);
        runningAlgorithm = new SimpleBooleanProperty(false);
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
        selectedFileName.textProperty().bind(selectedFileProperty);
        //disabling 2 of the buttons until a file is loaded to the system
        displaySettingsButton.disableProperty().bind(fileLoaded.not());
        runEngineButton.disableProperty().bind(fileLoaded.not());
        //disabling the remaining buttons until the algorithm completed at least once
        bestSolutionButton.disableProperty().bind(evolutionAlgorithmCompleted.not());
        historyButton.disableProperty().bind(evolutionAlgorithmCompleted.not());
        stopTaskButton.setDisable(true);
        pauseResumeButton.setDisable(true);
        taskProgressBar.visibleProperty().bind(runningAlgorithm);
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
//        String absolutePath = "C:\\Users\\zuta\\IdeaProjects\\evolution\\javaFXApplication\\src\\resources\\EX2-small.xml";

        // engine.loadXML(String fileToLoad,Consumer<DescriptorDTO> onSuccess,Consumer<Throwable> onFailure)
        engine.loadXML(
                absolutePath,
                descriptorDTO -> {
                    descriptor = descriptorDTO;
                    selectedFileProperty.set(absolutePath); //so this path will now be displayed (because of the binding)
                    fileLoaded.set(true); // because we're in the onSuccessConsumer
                },
                throwable -> {
                    if (!fileLoaded.get()) {
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
        this.appController.displaySettings(descriptor);
    }

    @FXML
    public void runEngineAction() {
        try {
            Dialog<ButtonType> dialog = new Dialog<>();
            ButtonType doneButtonType = new ButtonType("Done", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
            dialog.getDialogPane().getButtonTypes().add(doneButtonType);

            FXMLLoader loader = new FXMLLoader();
            URL mainFXML = getClass().getResource("/il/ac/mta/zuli/evolution/ui/endConditions/endConditionsFormComponent.fxml");
            loader.setLocation(mainFXML);
            GridPane gridPane = loader.load();
            EndConditionsController controller = loader.getController();

            dialog.getDialogPane().setContent(gridPane);
            Optional<ButtonType> result = dialog.showAndWait();
            System.out.println("before button pressed");
            System.out.println("result is present: "+result.isPresent());
            System.out.println("result get(): "+ result.get());
            //TODO - when done button is clicked, need to validate all the text field, and show a lable with the error
            if (result.isPresent() && result.get() == ButtonType.CANCEL) {
                System.out.println("cancel pressed");
                return;
            }

            int stride = controller.getStride();
            System.out.println("after submit");
        } catch (IOException e) {
            e.printStackTrace();
        }


//        TextInputDialog dialog = new TextInputDialog("end conditions");
//        dialog.setDialogPane(endConditionsDialogPane);
//        Optional<String> result = dialog.showAndWait();

        runningAlgorithm.set(true);
        stopTaskButton.setDisable(false);
        pauseResumeButton.setText("Pause");
        pauseResumeButton.setDisable(false);

        if (evolutionAlgorithmCompleted.get()) {
            //TODO add popup?

            // "The evolution-algorithm has already completed its course. "+ System.lineSeparator() +
            // "If you choose to re-run it, the information from the previous run will be lost." + System.lineSeparator() +
            // "Would you like to re-run the algorithm? (Enter Y/N)");
        }
        this.stride = 50;
        finishPredicates = new ArrayList<>();
        //TODO ask the user for input
        //TODO remove hardcoded values, handle invalid input
//        if (numOfGenerations < 0) {
//            ErrorEvent e = new ErrorEvent("Failed running evolution algorithm",
//                    ErrorType.RunError,
//                    new ValidationException(numOfGenerations + " is invalid number for generations, must be positive number"));
//            fireEvent("error", e);
//        }
//        if (generationsStride < 0 || generationsStride > numOfGenerations) {
//            ErrorEvent e = new ErrorEvent("Failed running evolution algorithm",
//                    ErrorType.RunError,
//                    new ValidationException(numOfGenerations + " is invalid number for generation strides, must be between 1 - " + numOfGenerations));
//            fireEvent("error", e);
//        }

//      executeEvolutionAlgorithm( int, int, Consumer<TimeTableSolutionDTO> onSuccess, Consumer <Throwable > onFailure)//
//todo validate that each type of predicate is given only once

        finishPredicates.add(new FinishPredicate(PredicateType.GENERATIONS, 500));
//        finishPredicates.add(new FinishPredicate(PredicateType.FITNESS, 85));
        //finishPredicates.add(new FinishPredicate(PredicateType.TIME, 1));

        engine.executeEvolutionAlgorithm(
                this.finishPredicates,
                this.stride,
                solution -> {
                    this.solution = solution;
                    evolutionAlgorithmCompleted.set(true);
                    runningAlgorithm.set(false);
                },
                throwable -> {
                    taskMessageLabel.setText("Failed running the algorithm." + System.lineSeparator()
                            + throwable.getMessage());
                    runningAlgorithm.set(false);
                },
                (StrideDataDTO strideData) -> {

                }
        );
    }

    @FXML
    public void bestSolutionAction() {
        this.appController.displaySolution(this.solution, descriptor.getTimeTable());
    }

    @FXML
    public void historyAction() {
    }

    //in the recording ~20min. this is the "old-fashioned" way, better to do it with property
    //TODO change to property implementation - no need for method (buttons only abled when running algorithm?)
    private void toggleTaskButtons(boolean isActive) {
        pauseResumeButton.setDisable(!isActive);
        stopTaskButton.setDisable(!isActive);
    }

    //in the recording about 26 minutes in
    public void bindTaskToUIComponents(Task<?> task) {
        taskMessageLabel.setText("");
        taskProgressBar.setProgress(0);

        // task message
        taskMessageLabel.textProperty().bind(task.messageProperty());

        // task progress bar
        taskProgressBar.progressProperty().bind(task.progressProperty());

        // TODO: bind to task.exceptionProperty() maybe?
    }

    // mentions this in the recording about 27 minutes in
    //this function updates the ui once the task is finished
    public void onTaskFinished() {
        this.taskMessageLabel.textProperty().unbind();
        this.taskProgressBar.progressProperty().unbind();
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
