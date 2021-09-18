package il.ac.mta.zuli.evolution.ui.runningAlgorithm.mutation;

import il.ac.mta.zuli.evolution.engine.TimeTableSolution;
import il.ac.mta.zuli.evolution.engine.evolutionengine.mutation.ComponentName;
import il.ac.mta.zuli.evolution.engine.evolutionengine.mutation.Mutation;
import il.ac.mta.zuli.evolution.engine.evolutionengine.mutation.MutationFactory;
import il.ac.mta.zuli.evolution.engine.timetable.TimeTable;
import il.ac.mta.zuli.evolution.ui.FXutils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;

public class MutationController {
    //region FXML
    @FXML
    private BorderPane mutationBorderPane;

    @FXML
    private RadioButton flippingRadioButton;

    @FXML
    private ToggleGroup mutationGroup;

    @FXML
    private TextField flippingProbabilityTextField;

    @FXML
    private TextField flippingTuplesTextField;

    @FXML
    private ChoiceBox<ComponentName> flippingChoiceBox;

    @FXML
    private RadioButton sizerRadioButton;

    @FXML
    private TextField sizerProbabilityTextField;

    @FXML
    private TextField sizerTuplesTextField;
    //endregion FXML

    private TimeTable timeTable;

    @FXML
    private void initialize() {
        ObservableList<ComponentName> choices = FXCollections.observableArrayList(ComponentName.values());
        flippingChoiceBox.setItems(choices);
    }

    public void setTimeTable(TimeTable timeTable) {
        this.timeTable = timeTable;
    }

    public Mutation<TimeTableSolution> getMutation() {
        if (flippingRadioButton.isSelected()) {
            double probability = FXutils.isNullOrEmpty(flippingProbabilityTextField.getText()) ? -1d : Double.parseDouble(flippingProbabilityTextField.getText());
            int maxTuples = FXutils.isNullOrEmpty(flippingTuplesTextField.getText()) ? -1 : Integer.parseInt(flippingTuplesTextField.getText(), 10);
            return MutationFactory.createMutationFromInput("flipping", timeTable, probability, maxTuples, flippingChoiceBox.getValue());
        }

        if (sizerRadioButton.isSelected()) {
            double probability = FXutils.isNullOrEmpty(sizerProbabilityTextField.getText()) ? -1d : Double.parseDouble(sizerProbabilityTextField.getText());
            int totalTuples = FXutils.isNullOrEmpty(sizerTuplesTextField.getText()) ? -1 : Integer.parseInt(sizerTuplesTextField.getText(), 10);
            return MutationFactory.createMutationFromInput("sizer", timeTable, probability, totalTuples, null);
        }

        return null;
    }
}

