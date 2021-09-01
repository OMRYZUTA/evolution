package il.ac.mta.zuli.evolution.ui.runningAlgorithm;

import il.ac.mta.zuli.evolution.dto.StrideDataDTO;
import il.ac.mta.zuli.evolution.dto.TimeTableSolutionDTO;
import il.ac.mta.zuli.evolution.engine.Engine;
import il.ac.mta.zuli.evolution.engine.predicates.EndConditionType;
import il.ac.mta.zuli.evolution.engine.predicates.EndPredicate;
import il.ac.mta.zuli.evolution.ui.app.AppController;
import il.ac.mta.zuli.evolution.ui.endConditions.EndConditionsController;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class RunAlgoController {
    //#region FXML components
    @FXML
    private ProgressBar progressBar;
    @FXML
    private Button pauseResumeButton;
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
    private TimeTableSolutionDTO solution = null;
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
    public void pauseResumeAction() {
        if (engine.isPaused()) {
            engine.resume();
        } else {
            engine.pause();
        }
    }

    @FXML
    public void stopAction() {
        engine.cancelCurrentTask();
    }

    public void runAlgorithm() {
        if (!getUserInput()) {
            return;
        }

        runningAlgoProperty.set(true);
        stopButton.setDisable(false);
        pauseResumeButton.setText("Pause");
        pauseResumeButton.setDisable(false);
        toggleTaskButtons(true);
        // executeEvolutionAlgorithm( int, int, Consumer<TimeTableSolutionDTO> onSuccess, Consumer <Throwable > onFailure)//
        engine.executeEvolutionAlgorithm(
                this.endPredicates,
                this.stride,
                solution -> {
                    this.solution = solution;
                    //TODO - figure it out
//                    evolutionAlgoCompletedProperty.set(true); //this is a headerController property
                    runningAlgoProperty.set(false);
                    appController.onAlgorithmFinished(solution);

                },
                throwable -> {
                    taskMessageLabel.setText("Failed running the algorithm." + System.lineSeparator()
                            + throwable.getMessage());
                    runningAlgoProperty.set(false);
                },
                (StrideDataDTO strideData) -> {
                }
        );

        toggleTaskButtons(false);
    }

    private boolean getUserInput() {
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

            //TODO - when OK button is clicked, need to validate all the text field, and show a lable with the error
            final Button btOk = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
            btOk.addEventFilter(ActionEvent.ACTION, event -> {
                if (!controller.validateAndStore()) {
                    event.consume();
                }
            });

            Optional<ButtonType> result = dialog.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.CANCEL) {
                System.out.println("cancel pressed");
                return false;
            }

            this.stride = controller.getStride();
            setPredicatesAccordingToDialogEndConditions(controller.getEndConditionTypePerValue());

            return true;
        } catch (IOException e) {
            e.printStackTrace(); //TODO: show error message to user
            return false;
        }
    }


    //in the recording ~20min. this is the "old-fashioned" way, better to do it with property
    //TODO change to property implementation - no need for method (buttons only abled when running algorithm?)
    private void toggleTaskButtons(boolean isActive) {
        stopButton.setDisable(!isActive);

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
