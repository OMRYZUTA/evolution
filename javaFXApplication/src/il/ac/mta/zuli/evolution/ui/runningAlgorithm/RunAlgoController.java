package il.ac.mta.zuli.evolution.ui.runningAlgorithm;

import il.ac.mta.zuli.evolution.dto.TimeTableSolutionDTO;
import il.ac.mta.zuli.evolution.engine.Engine;
import il.ac.mta.zuli.evolution.engine.TimeTableSolution;
import il.ac.mta.zuli.evolution.engine.evolutionengine.EngineSettings;
import il.ac.mta.zuli.evolution.engine.evolutionengine.crossover.CrossoverFactory;
import il.ac.mta.zuli.evolution.engine.evolutionengine.crossover.CrossoverInterface;
import il.ac.mta.zuli.evolution.engine.evolutionengine.crossover.Orientation;
import il.ac.mta.zuli.evolution.engine.evolutionengine.mutation.Mutation;
import il.ac.mta.zuli.evolution.engine.evolutionengine.selection.Selection;
import il.ac.mta.zuli.evolution.engine.evolutionengine.selection.SelectionFactory;
import il.ac.mta.zuli.evolution.engine.predicates.EndConditionType;
import il.ac.mta.zuli.evolution.engine.predicates.EndPredicate;
import il.ac.mta.zuli.evolution.ui.app.AppController;
import il.ac.mta.zuli.evolution.ui.runningAlgorithm.mutation.MutationController;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class RunAlgoController {
    //#region FXML components
    @FXML
    private GridPane runAlgoGridPane;

    @FXML
    private Button startButton;

    @FXML
    private Button pauseButton;

    @FXML
    private Button resumeButton;

    @FXML
    private Button stopButton;

    @FXML
    private ScrollPane updateSettingsScrollPane;

    @FXML
    private Button engineSettingsSaveButton;

    @FXML
    private AnchorPane selectionPane;

    @FXML
    private Label truncationCheckbox;

    @FXML
    private RadioButton rouletteWheelRadioButton;

    @FXML
    private ToggleGroup selectionGroup;

    @FXML
    private TextField topPercentTextField;

    @FXML
    private RadioButton truncationRadioButton;

    @FXML
    private CheckBox elitismCheckbox;

    @FXML
    private TextField elitismTextField;

    @FXML
    private AnchorPane crossoverPane;

    @FXML
    private RadioButton dayTimeOrientedRadioButton;

    @FXML
    private ToggleGroup crossoverGroup;

    @FXML
    private RadioButton teacherOrientedRadioButton;

    @FXML
    private RadioButton classOrientedRadioButton;

    @FXML
    private TextField cuttingPointsTextField;

    @FXML
    private Button addMutationButton;

    @FXML
    private VBox mutationsBox;

    @FXML
    private Label generationsProgressBarLabel;

    @FXML
    private ProgressBar generationsProgressBar;

    @FXML
    private Label fitnessProgressBarLabel;

    @FXML
    private ProgressBar fitnessProgressBar;

    @FXML
    private Label timeProgressBarLabel;

    @FXML
    private ProgressBar timeProgressBar;

    @FXML
    private Label errorLabel;
    //#endregion FXML components

    private final TimeTableSolutionDTO solution = null;
    private final List<EndPredicate> endPredicates;
    private final List<MutationController> mutations;
    private final SimpleBooleanProperty isPausedProperty;
    private final SimpleBooleanProperty runningAlgoProperty;
    private final Consumer<Boolean> onSuccess;
    private final Consumer<Throwable> onFailure;
    private final Consumer<TimeTableSolutionDTO> reportBestSolution;
    private AppController appController;
    private Engine engine;
    private int stride;

    public RunAlgoController() {
        endPredicates = new ArrayList<>();
        mutations = new ArrayList<>();
        isPausedProperty = new SimpleBooleanProperty(false);
        runningAlgoProperty = new SimpleBooleanProperty(false);
        onSuccess = finished -> {
            // if finished -> the run is complete
            // if !finished -> the run was paused
            //TODO - figure it out
            // evolutionAlgoCompletedProperty.set(true); //this is a headerController property
            runningAlgoProperty.set(false);
            appController.onFinishAlgorithm();
        };
        onFailure = throwable -> {
            errorLabel.setVisible(true);
            errorLabel.setText("Failed running the algorithm." + System.lineSeparator()
                    + throwable.getMessage());
            runningAlgoProperty.set(false);
        };
        reportBestSolution = (TimeTableSolutionDTO solution) -> {
            // this is the current best solution
            appController.updateBestSolution(solution);
        };
    }

    public void setEngine(Engine engine) {
        this.engine = engine;
    }

    @FXML
    private void initialize() {
        updateSettingsScrollPane.disableProperty().bind(isPausedProperty.not());

        engineSettingsSaveButton.addEventFilter(ActionEvent.ACTION, event -> {
//            if (validateAndStoreEngineSettings() == null) {
//                event.consume();
//            }
        });
    }

    @FXML
    void pauseAction(ActionEvent event) {
        isPausedProperty.set(true);
        engine.pauseEvolutionAlgorithm();
    }

    @FXML
    void resumeAction(ActionEvent event) {
        runningAlgoProperty.set(true);
        stopButton.setDisable(false);
        pauseButton.setDisable(false);

        engine.resumeEvolutionAlgorithm(
                this.endPredicates,
                this.stride,
                onSuccess,
                onFailure,
                reportBestSolution);
    }

    @FXML
    void saveEngineSettingsChangesAction(ActionEvent event) {

    }

    @FXML
    void addMutationAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader();
            URL mainFXML = getClass().getResource("/il/ac/mta/zuli/evolution/ui/runningAlgorithm/mutation/mutationComponent.fxml");
            loader.setLocation(mainFXML);
            BorderPane mutationComponent = loader.load();
            mutationsBox.getChildren().add(mutationComponent);
            MutationController controller = loader.getController();
            controller.setTimeTable(engine.getTimeTable());
            mutations.add(controller);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void startAction(ActionEvent event) {
        //TODO change the RunAlgo so that we also have a start button
        if (!getEndPredicates()) {
            return;
        }

        runningAlgoProperty.set(true);
        stopButton.setDisable(false);
        pauseButton.setDisable(false);

        engine.startEvolutionAlgorithm(
                this.endPredicates,
                this.stride,
                onSuccess,
                onFailure,
                reportBestSolution);
    }

    @FXML
    void stopAction(ActionEvent event) {
        engine.stopEvolutionAlgorithm();
    }

    @FXML
    public void saveEngineSettingsChangesAction2() {
        EngineSettings newEngineSettings = null;
        try {
            Selection<TimeTableSolution> updatedSelection = createSelectionFromInput();
            CrossoverInterface<TimeTableSolution> updatedCrossover = createCrossoverFromInput();
            List<Mutation<TimeTableSolution>> updatedMutations = createMutationListFromInput();
            newEngineSettings = new EngineSettings(
                    updatedSelection,
                    updatedCrossover,
                    updatedMutations,
                    engine.getEngineSettings().getInitialPopulationSize());
        } catch (Throwable e) {
            errorLabel.setText(e.getMessage());
            return;
        }

        engine.setEngineSettings(newEngineSettings); //the timetableEngine holds a descriptor which has engine settings
    }

    private Selection<TimeTableSolution> createSelectionFromInput() {
        EngineSettings<TimeTableSolution> previousSettings = engine.getEngineSettings();
        Selection<TimeTableSolution> updatedSelection = null;
        int elitism = 0;

        if (elitismCheckbox.isSelected()) {
            //relevant for both types of selection (if left empty we get 0?)
            elitism = Integer.parseInt(elitismTextField.getText());
        }

        //rouletteWheelRadioButton and truncationRadioButton are in the same toggle-group, one-at-most can be selected
        if (rouletteWheelRadioButton.isSelected()) {
            updatedSelection = SelectionFactory.createSelectionFromInput(
                    "rouletteWheel",
                    previousSettings.getInitialPopulationSize(),
                    elitism, 0); //topPercent NA for rouletteWheel
        } else if (truncationRadioButton.isSelected()) {
            int topPercent = Integer.parseInt(topPercentTextField.getText());
            updatedSelection = SelectionFactory.createSelectionFromInput(
                    "truncation",
                    previousSettings.getInitialPopulationSize(),
                    elitism, topPercent);
        } else {
            // if nothing was selected from this toggle-group, it remains unchanged from previous settings
            updatedSelection = engine.getEngineSettings().getSelection();
        }

        return updatedSelection;
    }

    private CrossoverInterface<TimeTableSolution> createCrossoverFromInput() {
        EngineSettings<TimeTableSolution> previousSettings = engine.getEngineSettings();
        CrossoverInterface<TimeTableSolution> updatedCrossover = null;

        int numOfCuttingPoints = Integer.parseInt(cuttingPointsTextField.getText());

        //radioButtons in crossover-toggle-group
        if (dayTimeOrientedRadioButton.isSelected()) {
            updatedCrossover = CrossoverFactory.createCrossoverFromInput(
                    "daytimeoriented",
                    numOfCuttingPoints,
                    null,
                    engine.getTimeTable());
        } else if (teacherOrientedRadioButton.isSelected()) {
            updatedCrossover = CrossoverFactory.createCrossoverFromInput(
                    "aspectoriented",
                    numOfCuttingPoints,
                    Orientation.TEACHER,
                    engine.getTimeTable());
        } else if (classOrientedRadioButton.isSelected()) {
            updatedCrossover = CrossoverFactory.createCrossoverFromInput(
                    "aspectoriented",
                    numOfCuttingPoints,
                    Orientation.CLASS,
                    engine.getTimeTable());
        } else {
            // if nothing was selected from this toggle-group, it remains unchanged from previous settings
            updatedCrossover = engine.getEngineSettings().getCrossover();
        }

        return updatedCrossover;
    }

    private List<Mutation<TimeTableSolution>> createMutationListFromInput() {
        return mutations
                .stream()
                .map(mutationController -> mutationController.getMutation())
                .collect(Collectors.toList());
    }

//    @FXML
//    public void saveEngineSettingsChangesAction() {
//        EngineSettings engineSettings = null;
//        if (validateAndStoreEngineSettings()) {
//            engine.setEngineSettings(newEngineSettings);
//        }
//    }

//    private boolean validateAndStoreEngineSettings() {
//        boolean result = false;
//        try {
//            Selection<TimeTableSolution> updatedSelection = createNewSelection();
//            //TODO implement crossover and list of mutations
//            newEngineSettings = new EngineSettings(updatedSelection, null, null, engine.getEngineSettings().getInitialPopulationSize());
//            result = true;
//        } catch (Throwable e) {
//            errorLabel.setText(e.getMessage());
//            result = false;
//        }
//
//        return result;
//    }

    private boolean getEndPredicates() {
        try {
            endPredicates.clear();
            //commented out for faster debugging!!
//
//            Dialog<ButtonType> dialog = new Dialog<>();
//            dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
//            dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
//
//            FXMLLoader loader = new FXMLLoader();
//            URL mainFXML = getClass().getResource("/il/ac/mta/zuli/evolution/ui/endConditions/endConditionsFormComponent.fxml");
//            loader.setLocation(mainFXML);
//            GridPane gridPane = loader.load();
//            EndConditionsController controller = loader.getController();
//
//            dialog.getDialogPane().setContent(gridPane);
//
//
//            final Button btOk = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
//            btOk.addEventFilter(ActionEvent.ACTION, event -> {
//                if (!controller.validateAndStore()) {
//                    event.consume();
//                }
//            });
//
//            Optional<ButtonType> result = dialog.showAndWait();
//
//            if (result.isPresent() && result.get() == ButtonType.CANCEL) {
//                return false;
//            }
//
//            this.stride = controller.getStride();
//            setPredicatesAccordingToDialogEndConditions(controller.getEndConditionTypePerValue());
//
            stride = 50;
            endPredicates.add(new EndPredicate(EndConditionType.GENERATIONS, 2000));
            endPredicates.add(new EndPredicate(EndConditionType.FITNESS, 96.8));
            bindProgressBars();
            return true;
        } catch (Throwable e) {
            e.printStackTrace(); //TODO: show error message to user
            return false;
        }
    }

    private void setPredicatesAccordingToDialogEndConditions
            (Map<EndConditionType, Double> endConditionTypePerValue) {
        for (EndConditionType endCondition : endConditionTypePerValue.keySet()) {
            switch (endCondition) {
                case FITNESS:
                    endPredicates.add(new EndPredicate(EndConditionType.FITNESS, endConditionTypePerValue.get(endCondition)));
                    break;
                case GENERATIONS:
                    endPredicates.add(new EndPredicate(EndConditionType.GENERATIONS, endConditionTypePerValue.get(endCondition)));
                    break;
                case TIME:
                    endPredicates.add(new EndPredicate(EndConditionType.TIME, endConditionTypePerValue.get(endCondition)));
                    break;
            }
        }
    }

    public void setAppController(AppController appController) {
        this.appController = appController;
    }

    private void bindProgressBars() {
        //initially setting all 3 bars to be invisible
        timeProgressBarLabel.setVisible(false);
        timeProgressBar.setVisible(false);
        fitnessProgressBarLabel.setVisible(false);
        fitnessProgressBar.setVisible(false);
        generationsProgressBarLabel.setVisible(false);
        generationsProgressBar.setVisible(false);

        for (EndPredicate predicate : this.endPredicates) {
            switch (predicate.getType()) {
                case TIME:
                    timeProgressBar.setVisible(true);
                    timeProgressBarLabel.setVisible(true);
                    timeProgressBar.progressProperty().bind(engine.getTimeProperty().divide(predicate.getParameter() * 60 * 1000F));
                case FITNESS:
                    fitnessProgressBar.setVisible(true);
                    fitnessProgressBarLabel.setVisible(true);
                    fitnessProgressBar.progressProperty().bind(engine.getFitnessProperty().divide(predicate.getParameter()));
                case GENERATIONS:
                    generationsProgressBar.setVisible(true);
                    generationsProgressBarLabel.setVisible(true);
                    //TODO generation progress bar reaches end to soon
                    generationsProgressBar.progressProperty().bind(engine.getGenerationNumProperty().divide(predicate.getParameter()));
            }
        }
    }
}
