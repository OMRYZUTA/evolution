package il.ac.mta.zuli.evolution.ui.runningAlgorithm.mutation;

import il.ac.mta.zuli.evolution.engine.TimeTableSolution;
import il.ac.mta.zuli.evolution.engine.evolutionengine.mutation.ComponentName;
import il.ac.mta.zuli.evolution.engine.evolutionengine.mutation.Mutation;
import il.ac.mta.zuli.evolution.engine.evolutionengine.mutation.MutationFactory;
import il.ac.mta.zuli.evolution.engine.timetable.TimeTable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

public class MutationController {
    //region FXML
    @FXML
    private BorderPane mutationBorderPane;

    @FXML
    private RadioButton flippingRadioButton;

    @FXML
    private RadioButton sizerRadioButton;

    @FXML
    private TextField flippingProbabilityTextField;

    @FXML
    private TextField sizerProbabilityTextField;

    @FXML
    private TextField flippingTuplesTextField;

    @FXML
    private ChoiceBox<ComponentName> flippingChoiceBox;

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
            double probability = Double.parseDouble(flippingProbabilityTextField.getText());
            int maxTuples = Integer.parseInt(flippingTuplesTextField.getText(), 10);
            return MutationFactory.createMutationFromInput("flipping", timeTable, probability, maxTuples, flippingChoiceBox.getValue());
        }

        if (sizerRadioButton.isSelected()) {
            double probability = Double.parseDouble(sizerProbabilityTextField.getText());
            int totalTuples = Integer.parseInt(sizerTuplesTextField.getText(), 10);
            return MutationFactory.createMutationFromInput("sizer", timeTable, probability, totalTuples, null);
        }

        return null;
    }
}

