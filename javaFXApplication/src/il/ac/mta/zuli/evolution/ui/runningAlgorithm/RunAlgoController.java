package il.ac.mta.zuli.evolution.ui.runningAlgorithm;

import il.ac.mta.zuli.evolution.dto.TimeTableSolutionDTO;
import il.ac.mta.zuli.evolution.engine.Engine;
import il.ac.mta.zuli.evolution.engine.TimeTableSolution;
import il.ac.mta.zuli.evolution.engine.evolutionengine.EngineSettings;
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
    private FlowPane mutationPane;

    @FXML
    private Button addMutationButton;

    @FXML
    private FlowPane progressFlowPane;
    //#endregion FXML components

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
            if (validateAndStoreEngineSettings() == null) {
                event.consume();
            }
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
    public void saveEngineSettingsChangesAction() {
        Selection<TimeTableSolution> updatedSelection = null;
        try {
            updatedSelection = createNewSelection();
            //crossover
            //mutations

            engine.setEngineSettings();

        } catch (Throwable e) {
        }
    }

    private boolean validateAndStoreEngineSettings() {
        try {
            Selection<TimeTableSolution> updatedSelection = createNewSelection();
        } catch (Throwable e) {
        }


    }

    private Selection<TimeTableSolution> createNewSelection() {
        EngineSettings settings = engine.getEngineSettings();
        Selection<TimeTableSolution> updatedSelection = null;
        int elitism = 0;
        if (elitismCheckbox.isSelected()) {
            elitism = Integer.parseInt(elitismTextField.getText());
        }

        if (rouletteWheelRadioButton.isSelected()) {
            updatedSelection = SelectionFactory.createSelectionFromInput(
                    "rouletteWheel",
                    settings.getInitialPopulationSize(),
                    elitism, 0); //topPercent NA for rouletteWheel
        } else if (truncationRadioButton.isSelected()) {
            int topPercent = Integer.parseInt(topPercentTextField.getText());
            updatedSelection = SelectionFactory.createSelectionFromInput(
                    "truncation",
                    settings.getInitialPopulationSize(),
                    elitism, topPercent);
        }

        System.out.println(updatedSelection);
        return updatedSelection;
    }

    private boolean getUserInput() {
        try {
            endPredicates.clear();
            //comemnted out for faster debugging!!
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
