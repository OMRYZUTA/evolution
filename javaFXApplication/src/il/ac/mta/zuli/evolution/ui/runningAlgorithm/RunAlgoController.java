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
import il.ac.mta.zuli.evolution.ui.endConditions.EndConditionsController;
import il.ac.mta.zuli.evolution.ui.runningAlgorithm.mutation.MutationController;
import il.ac.mta.zuli.evolution.ui.runningAlgorithm.progress.ProgressController;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class RunAlgoController {
    //#region FXML components
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
    private RadioButton rouletteWheelRadioButton;
    @FXML
    private TextField topPercentTextField;
    @FXML
    private RadioButton truncationRadioButton;
    @FXML
    private CheckBox elitismCheckbox;
    @FXML
    private TextField elitismTextField;
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
    private VBox mutationsBox;
    @FXML
    private ProgressController generationProgressComponentController;
    @FXML
    private ProgressController fitnessProgressComponentController;
    @FXML
    private ProgressController timeProgressComponentController;
    @FXML
    private Label errorLabel;
    //#endregion FXML components

    private final SimpleBooleanProperty algoIsPausedProperty;
    private final SimpleBooleanProperty algoIsRunningProperty;
    private final SimpleStringProperty errorProperty;
    private final List<EndPredicate> endPredicates;
    private final List<MutationController> mutations; // mutations added instead of previous mutations in engineSettings
    private final Consumer<Boolean> onAlgoFinished;
    private final Consumer<Throwable> onAlgoFailed;
    private final Consumer<TimeTableSolutionDTO> reportBestSolution;
    private AppController appController;
    private Engine engine;
    private int stride;
    private boolean startedRun;

    public RunAlgoController() {
        algoIsPausedProperty = new SimpleBooleanProperty(false);
        algoIsRunningProperty = new SimpleBooleanProperty(false);
        errorProperty = new SimpleStringProperty("");
        endPredicates = new ArrayList<>();
        mutations = new ArrayList<>();

        onAlgoFinished = finished -> {
            algoIsRunningProperty.set(false);
        };

        onAlgoFailed = throwable -> {
            algoIsRunningProperty.set(false);
            errorProperty.set("Failed running the algorithm." + System.lineSeparator()
                    + throwable.getMessage());
        };

        reportBestSolution = (TimeTableSolutionDTO solution) -> {
            // this is the current best solution
            appController.updateBestSolution(solution);
        };
    }

    public void setEngine(Engine engine) {
        this.engine = engine;
        LongProperty timeProperty = engine.getTimeProperty();
        StringBinding sb = new StringBinding() {
            {
                bind(timeProperty);
            }

            @Override
            protected String computeValue() {
                long value = timeProperty.get() / 1000;
                long minutes = value / 60;
                long seconds = value % 60;
                return String.format("%02d:%02d", minutes, seconds);
            }
        };
        timeProgressComponentController.valueTextProperty().bind(sb);
        fitnessProgressComponentController.valueTextProperty().bind(Bindings.format("%.2f", engine.getFitnessProperty()));
        generationProgressComponentController.valueTextProperty().bind(Bindings.format("%d", engine.getGenerationNumProperty()));
    }

    public void setAppController(AppController appController) {
        this.appController = appController;
    }

    @FXML
    private void initialize() {
        startButton.disableProperty().bind(algoIsRunningProperty); // start is disabled when the algo is running
        pauseButton.disableProperty().bind(algoIsRunningProperty.not()); // pause is disabled when algo is *not* running
        resumeButton.disableProperty().bind(algoIsPausedProperty.not()); // resume is disabled when the algo is *not* paused
        stopButton.disableProperty().bind(algoIsRunningProperty.not());  // stop is disabled when algo is *not* running
        errorLabel.textProperty().bind(errorProperty);
        updateSettingsScrollPane.disableProperty().bind(algoIsPausedProperty.not());
        //Todo ask omry
        engineSettingsSaveButton.addEventFilter(ActionEvent.ACTION, event -> {
//            if (validateAndStoreEngineSettings() == null) {
//                event.consume();
//            }
        });

        generationProgressComponentController.setTitle("Generation:");
        fitnessProgressComponentController.setTitle("Fitness Score:");
        timeProgressComponentController.setTitle("Time:");
    }

    @FXML
    void startAction(ActionEvent event) {
        if (startedRun && !alertUserReRun()) {
            return;
        }

        if (!getEndPredicatesInput()) {
            return;
        }

        startedRun = true;
        algoIsPausedProperty.set(false);
        algoIsRunningProperty.set(true);
        errorProperty.set("");

        engine.startEvolutionAlgorithm(
                this.endPredicates,
                this.stride,
                onAlgoFinished,
                onAlgoFailed,
                reportBestSolution);
    }

    @FXML
    void stopAction(ActionEvent event) {
        algoIsRunningProperty.set(false);
        engine.stopEvolutionAlgorithm();
    }

    @FXML
    void pauseAction(ActionEvent event) {
        algoIsPausedProperty.set(true);
        algoIsRunningProperty.set(false);
        engine.pauseEvolutionAlgorithm();
    }

    @FXML
    void resumeAction(ActionEvent event) {
        algoIsPausedProperty.set(false);
        algoIsRunningProperty.set(true);
        errorProperty.set("");

        engine.resumeEvolutionAlgorithm(
                this.endPredicates,
                this.stride,
                onAlgoFinished,
                onAlgoFailed,
                reportBestSolution);
    }

    @FXML
    public void saveEngineSettingsChangesAction() {
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
            errorProperty.set(e.getMessage());
            return;
        }

        engine.setEngineSettings(newEngineSettings); //the timetableEngine holds a descriptor which has engine settings
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
            mutations.add(controller); //keeping a list of controllers
        } catch (IOException e) {
            errorProperty.set(e.getMessage());
        }
    }

    private Selection<TimeTableSolution> createSelectionFromInput() {
        EngineSettings<TimeTableSolution> previousSettings = engine.getEngineSettings();
        Selection<TimeTableSolution> updatedSelection = null;
        int elitism = 0;

        if (elitismCheckbox.isSelected()) {
            //relevant for both types of selection (if left empty we get 0?)
            elitism = Integer.parseInt(elitismTextField.getText(), 10);
        }

        //rouletteWheelRadioButton and truncationRadioButton are in the same toggle-group, one-at-most can be selected
        if (rouletteWheelRadioButton.isSelected()) {
            updatedSelection = SelectionFactory.createSelectionFromInput(
                    "rouletteWheel",
                    previousSettings.getInitialPopulationSize(),
                    elitism, 0); //topPercent NA for rouletteWheel
        } else if (truncationRadioButton.isSelected()) {
            int topPercent = Integer.parseInt(topPercentTextField.getText(), 10);
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
        // if wasn't updated now, it remains unchanged
        CrossoverInterface<TimeTableSolution> updatedCrossover = engine.getEngineSettings().getCrossover();

        if (crossoverGroup.getSelectedToggle() != null) {
            int numOfCuttingPoints = Integer.parseInt(cuttingPointsTextField.getText(), 10);

            //radioButtons in crossover-toggle-group, so only one can be selected
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
            }
        }

        return updatedCrossover;
    }

    private List<Mutation<TimeTableSolution>> createMutationListFromInput() {
        List<Mutation<TimeTableSolution>> prevMutationList = engine.getEngineSettings().getMutations();

        List<Mutation<TimeTableSolution>> updatedMutationList = mutations
                .stream()
                .map(mutationController -> mutationController.getMutation())
                .collect(Collectors.toList());

        return (updatedMutationList.size() == 0) ? prevMutationList : updatedMutationList;
    }

    private boolean alertUserReRun() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("The evolution-algorithm previously ran.");
        alert.setContentText("If you choose to re-run it, the information from the previous run will be lost." + System.lineSeparator() +
                "Would you like to re-run the algorithm?");

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    private boolean getEndPredicatesInput() {
        try {
            endPredicates.clear();

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
            dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);

            FXMLLoader loader = new FXMLLoader();
            URL mainFXML = getClass().getResource("/il/ac/mta/zuli/evolution/ui/endConditions/endConditionsFormComponent.fxml");
            loader.setLocation(mainFXML);
            GridPane gridPane = loader.load();
            EndConditionsController controller = loader.getController();

            dialog.getDialogPane().setContent(gridPane);


            final Button btOk = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
            btOk.addEventFilter(ActionEvent.ACTION, event -> {
                if (!controller.validateAndStore()) {
                    event.consume();
                }
            });

            Optional<ButtonType> result = dialog.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.CANCEL) {
                return false;
            }

            this.stride = controller.getStride();
            setPredicatesAccordingToDialogEndConditions(controller.getEndConditionTypePerValue());

//            stride = 20;
////            endPredicates.add(new EndPredicate(EndConditionType.GENERATIONS, 2000));
//            endPredicates.add(new EndPredicate(EndConditionType.TIME, 2));
//            endPredicates.add(new EndPredicate(EndConditionType.FITNESS, 96.8));
            bindProgressBars();

            return true;
        } catch (Throwable e) {
            onAlgoFailed.accept(e);
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

    private void bindProgressBars() {
        for (EndPredicate predicate : this.endPredicates) {
            switch (predicate.getType()) {
                case TIME:
                    timeProgressComponentController.setMax(String.format("%.0f:00m", predicate.getParameter()));
                    timeProgressComponentController.progressProperty().bind(engine.getTimeProperty().divide(predicate.getParameter() * 60 * 1000F));
                    break;
                case FITNESS:
                    fitnessProgressComponentController.setMax(String.format("%.2f", predicate.getParameter()));
                    fitnessProgressComponentController.progressProperty().bind(engine.getFitnessProperty().divide(predicate.getParameter()));
                    break;
                case GENERATIONS:
                    generationProgressComponentController.setMax(String.format("%.0f", predicate.getParameter()));
                    generationProgressComponentController.progressProperty().bind(engine.getGenerationNumProperty().divide(predicate.getParameter()));
                    break;
            }
        }
    }
}
