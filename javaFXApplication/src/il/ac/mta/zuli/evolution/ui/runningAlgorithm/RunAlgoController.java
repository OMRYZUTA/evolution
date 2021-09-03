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
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class RunAlgoController {
    //#region FXML components
    @FXML
    private Label errorLabel;
    @FXML
    private GridPane runAlgoGridPane;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private Button pauseButton;

    @FXML
    private Button resumeButton;

    @FXML
    private Button stopButton;

    @FXML
    private Button bestSolutionButton;

    @FXML
    private ScrollPane updateSettingsScrollPane;

    @FXML
    private CheckBox elitismCheckbox;

    @FXML
    private TextField elitismTextField;

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
    private FlowPane mutationPane;

    @FXML
    private Button addMutationButton;

    @FXML
    private FlowPane progressFlowPane;
    //#endregion FXML components
    //private EngineSettings newEngineSettings; //why do we need this as a field?
    private Engine engine;
    private int stride;
    private final TimeTableSolutionDTO solution = null;
    private final List<EndPredicate> endPredicates;
    private final SimpleBooleanProperty isPausedProperty;
    private final SimpleBooleanProperty runningAlgoProperty;
    private AppController appController;
    private final Consumer<Boolean> onSuccess;
    private final Consumer<Throwable> onFailure;
    private final Consumer<TimeTableSolutionDTO> reportBestSolution;

    public RunAlgoController() {
        endPredicates = new ArrayList<>();
        isPausedProperty = new SimpleBooleanProperty(false);
        runningAlgoProperty = new SimpleBooleanProperty(false);
        onSuccess = finished -> {
            // if finished -> the run is complete
            // if !finished -> the run was paused
            //TODO - figure it out
//                    evolutionAlgoCompletedProperty.set(true); //this is a headerController property
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
    public void currBestSolutionAction() {
    }

    @FXML
    public void pauseAction() {
        isPausedProperty.set(true);
        engine.pause();
    }

    @FXML
    void resumeAction() {
        runningAlgoProperty.set(true);
        stopButton.setDisable(false);
        pauseButton.setDisable(false);

        engine.resume(
                this.endPredicates,
                this.stride,
                onSuccess,
                onFailure,
                reportBestSolution);
    }

    @FXML
    public void stopAction() {
        engine.stop();
    }

    public void runAlgorithm() {
        //TODO change the RunAlgo so that we also have a start button
        if (!getUserInput()) {
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
        return null;
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

    private boolean getUserInput() {
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
}
