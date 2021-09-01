package il.ac.mta.zuli.evolution.ui.runningAlgorithm;

import il.ac.mta.zuli.evolution.dto.TimeTableSolutionDTO;
import il.ac.mta.zuli.evolution.engine.Engine;
import il.ac.mta.zuli.evolution.engine.predicates.EndConditionType;
import il.ac.mta.zuli.evolution.engine.predicates.EndPredicate;
import il.ac.mta.zuli.evolution.ui.app.AppController;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RunAlgoController {
    //#region FXML components
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
    private Label truncationCheckbox;
    @FXML
    private RadioButton rouletteWheelRadioButton;
    @FXML
    private RadioButton truncationRadioButton;
    @FXML
    private TextField topPercentTextField;
    @FXML
    private RadioButton dayTimeOrientedRadioButton;
    @FXML
    private RadioButton classOrientedRadioButton;
    @FXML
    private RadioButton teacherOrientedRadioButton;
    @FXML
    private Label generationStrideLabel;
    @FXML
    private Label generationStrideDataLabel;
    @FXML
    private Label taskMessageLabel; //TODO start out invisible, will only be needed if failed running
    //#endregion FXML components

    private Engine engine;
    private int stride;
    private final TimeTableSolutionDTO solution = null;
    private final List<EndPredicate> endPredicates;
    private final SimpleBooleanProperty isPausedProperty;
    private final SimpleBooleanProperty runningAlgoProperty;



    private AppController appController;
    public RunAlgoController() {
        endPredicates = new ArrayList<>();
        isPausedProperty = new SimpleBooleanProperty(false);
        runningAlgoProperty = new SimpleBooleanProperty(false);
    }

    public void setEngine(Engine engine) {
        this.engine = engine;
    }

    @FXML
    private void initialize() {
        updateSettingsScrollPane.setDisable(!isPausedProperty.get());
        generationStrideLabel.textProperty().bind(Bindings.format("%d", stride));
    }

    @FXML
    public void currBestSolutionAction() {
    }

    @FXML
    public void pauseAction() {
        engine.pause();
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
        // executeEvolutionAlgorithm( int, int, Consumer<TimeTableSolutionDTO> onSuccess, Consumer <Throwable > onFailure)//
        engine.executeEvolutionAlgorithm(
                this.endPredicates,
                this.stride,
                finished -> {
                    // if finished -> the run is complete
                    // if !finished -> the run was paused
                    //TODO - figure it out
//                    evolutionAlgoCompletedProperty.set(true); //this is a headerController property
                    runningAlgoProperty.set(false);
                    appController.onFinishAlgorithm();
                },
                throwable -> {
                    taskMessageLabel.setText("Failed running the algorithm." + System.lineSeparator()
                            + throwable.getMessage());
                    runningAlgoProperty.set(false);
                },
                (TimeTableSolutionDTO solution) -> {
                    // this is the current best solution
                    appController.updateBestSolution(solution);
                }
        );
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
            stride = 5;
            endPredicates.add(new EndPredicate(EndConditionType.GENERATIONS, 500));
            return true;
        } catch (Throwable e) {
            e.printStackTrace(); //TODO: show error message to user
            return false;
        }
    }

    private void setPredicatesAccordingToDialogEndConditions(Map<EndConditionType, Double> endConditionTypePerValue) {
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
