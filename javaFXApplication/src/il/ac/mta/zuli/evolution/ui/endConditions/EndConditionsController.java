package il.ac.mta.zuli.evolution.ui.endConditions;

import il.ac.mta.zuli.evolution.engine.predicates.EndConditionType;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;

import java.util.HashMap;
import java.util.Map;

public class EndConditionsController {
    @FXML
    private FlowPane warningsFlowPane;
    @FXML
    private TextField strideField;
    @FXML
    private CheckBox generationCheckbox;
    @FXML
    private TextField generationTextField;
    @FXML
    private CheckBox fitnessCheckbox;
    @FXML
    private TextField fitnessTextField;
    @FXML
    private CheckBox minutesCheckbox;
    @FXML
    private TextField timeTextField;
    @FXML
    private Button endConditionsButton;

    private final SimpleIntegerProperty strideProperty;
    private final SimpleBooleanProperty totalGenerationsCheckProperty;
    private final SimpleIntegerProperty totalGenerationsProperty;
    private final SimpleBooleanProperty scoreCheckProperty;
    private final SimpleDoubleProperty scoreProperty;
    private final SimpleBooleanProperty totalMinutesCheckProperty;
    private final SimpleIntegerProperty totalMinutesProperty;
    private final Map<EndConditionType, Double> endConditionTypePerValue;

    public EndConditionsController() {
        strideProperty = new SimpleIntegerProperty(0);
        totalGenerationsCheckProperty = new SimpleBooleanProperty(false);
        totalGenerationsProperty = new SimpleIntegerProperty(0);
        scoreCheckProperty = new SimpleBooleanProperty(false);
        scoreProperty = new SimpleDoubleProperty(0f);
        totalMinutesCheckProperty = new SimpleBooleanProperty(false);
        totalMinutesProperty = new SimpleIntegerProperty(0);
        endConditionTypePerValue = new HashMap<EndConditionType, Double>();
    }

    @FXML
    private void initialize() {
        generationTextField.disableProperty().bind(generationCheckbox.selectedProperty().not());
        fitnessTextField.disableProperty().bind(fitnessCheckbox.selectedProperty().not());
        timeTextField.disableProperty().bind(minutesCheckbox.selectedProperty().not());
    }

    public int getStride() {
        int stride = -1;
        try {
            stride = Integer.parseInt(strideField.textProperty().get());

        } catch (Throwable e) {

        }
        return stride;
    }

    public int getStrideProperty() {
        return strideProperty.get();
    }

    public boolean isTotalGenerationsCheckProperty() {
        return totalGenerationsCheckProperty.get();
    }

    public int getTotalGenerationsProperty() {
        return totalGenerationsProperty.get();
    }

    public boolean isScoreCheckProperty() {
        return scoreCheckProperty.get();
    }

    public double getScoreProperty() {
        return scoreProperty.get();
    }

    public boolean isTotalMinutesCheckProperty() {
        return totalMinutesCheckProperty.get();
    }

    public int getTotalMinutesProperty() {
        return totalMinutesProperty.get();
    }

    public boolean validateAndStore() {
        warningsFlowPane.getChildren().clear();
        endConditionTypePerValue.clear();
        boolean result = true;
        if (getStride() < 0) {
            addWarning("stride must be positive double ");
            result = false;
        }
        if (generationCheckbox.selectedProperty().get()) {
            if (getTotalGenerations() < 0) {
                addWarning("total generations must be positive double ");
                result = false;
            } else if (getStride() > getTotalGenerations()) {
                addWarning("stride must be less than total generations ");
                result = false;
            } else {
                endConditionTypePerValue.put(EndConditionType.GENERATIONS, (double) getTotalGenerations());
            }
        }
        if (fitnessCheckbox.selectedProperty().get()) {
            if (getfitness() < 0 || getfitness() > 100) {
                addWarning("fitness must be between 1.0 to 100.0 ");
                result = false;
            } else {
                endConditionTypePerValue.put(EndConditionType.FITNESS, getfitness());
            }
        }
        if (minutesCheckbox.selectedProperty().get()) {
            if (getTime() < 0) {
                addWarning("minutes must be a positive number ");
                result = false;
            } else {
                endConditionTypePerValue.put(EndConditionType.TIME, (double) getTime());
            }
        }
        if (allCheckboxesUnChecked()) {
            addWarning("you must choose at least one end condtion ");
            result = false;
        }
        return result;
    }

    private boolean allCheckboxesUnChecked() {
        return generationCheckbox.selectedProperty().not().and(fitnessCheckbox.selectedProperty().not()).and(minutesCheckbox.selectedProperty().not()).get();
    }

    private int getTime() {
        int time = -1;
        try {
            time = Integer.parseInt(timeTextField.textProperty().get());

        } catch (Throwable e) {

        }
        return time;
    }

    private double getfitness() {
        double generations = -1;
        try {
            generations = Double.parseDouble(fitnessTextField.textProperty().get());

        } catch (Throwable e) {

        }
        return generations;
    }

    private void addWarning(String s) {
        Label strideLable = new Label();
        strideLable.textProperty().set(s);
        strideLable.textFillProperty().set(Color.color(1, 0, 0));
        warningsFlowPane.getChildren().add(strideLable);
    }

    private int getTotalGenerations() {
        int generations = -1;
        try {
            generations = Integer.parseInt(generationTextField.textProperty().get());

        } catch (Throwable e) {

        }
        return generations;
    }

    public Map<EndConditionType, Double> getEndConditionTypePerValue() {
        return endConditionTypePerValue;
    }
}
