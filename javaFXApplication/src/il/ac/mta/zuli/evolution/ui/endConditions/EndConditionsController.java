package il.ac.mta.zuli.evolution.ui.endConditions;

import il.ac.mta.zuli.evolution.engine.predicates.EndConditionType;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.util.converter.NumberStringConverter;

import java.util.HashMap;
import java.util.Map;

public class EndConditionsController {
    //region fxml components
    @FXML
    private FlowPane warningsFlowPane;
    @FXML
    private TextField strideTextField;
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
    private TextField minutesTextField;
    //endregion

    private final SimpleIntegerProperty strideProperty;
    private final SimpleIntegerProperty generationProperty;
    private final SimpleDoubleProperty fitnessProperty;
    private final SimpleIntegerProperty minutesProperty;
    private final Map<EndConditionType, Double> endConditionTypePerValue;

    public EndConditionsController() {
        strideProperty = new SimpleIntegerProperty(0);
        generationProperty = new SimpleIntegerProperty(0);
        fitnessProperty = new SimpleDoubleProperty(0f);
        minutesProperty = new SimpleIntegerProperty(0);
        endConditionTypePerValue = new HashMap<>();
    }

    @FXML
    private void initialize() {
        strideTextField.textProperty().bindBidirectional(strideProperty, new NumberStringConverter());

        generationTextField.disableProperty().bind(generationCheckbox.selectedProperty().not());
        generationTextField.textProperty().bindBidirectional(generationProperty, new NumberStringConverter());

        fitnessTextField.disableProperty().bind(fitnessCheckbox.selectedProperty().not());
        fitnessTextField.textProperty().bindBidirectional(fitnessProperty, new NumberStringConverter());

        minutesTextField.disableProperty().bind(minutesCheckbox.selectedProperty().not());
        minutesTextField.textProperty().bindBidirectional(minutesProperty, new NumberStringConverter());
    }

    //region getters
    public int getStride() {
        return strideProperty.get();
    }

    public Map<EndConditionType, Double> getEndConditionTypePerValue() {
        return endConditionTypePerValue;
    }

    private double getFitness() {
        return fitnessProperty.get();
    }

    private int getTotalGenerations() {
        return generationProperty.get();
    }

    private int getMinutes() {
        return minutesProperty.get();
    }
    //endregion

    public boolean validateAndStore() {
        warningsFlowPane.getChildren().clear();
        endConditionTypePerValue.clear();
        boolean result = true;

        if (getStride() <= 0) {
            addWarning("Stride must be positive number ");
            result = false;
        }

        if (generationCheckbox.isSelected()) {
            if (getTotalGenerations() <= 100) {
                addWarning("Total generations must be greater then 100 ");
                result = false;
            } else if (getStride() > getTotalGenerations()) {
                addWarning("Stride must be less than total generations ");
                result = false;
            } else {
                endConditionTypePerValue.put(EndConditionType.GENERATIONS, (double) getTotalGenerations());
            }
        }

        if (fitnessCheckbox.isSelected()) {
            if (getFitness() < 1f || getFitness() > 100f) {
                addWarning("Fitness must be between 1.0 to 100.0 (including) ");
                result = false;
            } else {
                endConditionTypePerValue.put(EndConditionType.FITNESS, getFitness());
            }
        }

        if (minutesCheckbox.isSelected()) {
            if (getMinutes() <= 0) {
                addWarning("Minutes must be a positive number ");
                result = false;
            } else {
                endConditionTypePerValue.put(EndConditionType.TIME, (double) getMinutes());
            }
        }

        if (allCheckboxesUnChecked()) {
            addWarning("Please choose at least one end condition ");
            result = false;
        }

        return result;
    }

    private boolean allCheckboxesUnChecked() {
        return generationCheckbox.selectedProperty().not().and(fitnessCheckbox.selectedProperty().not()).and(minutesCheckbox.selectedProperty().not()).get();
    }

    private void addWarning(String s) {
        Label strideLable = new Label();
        strideLable.textProperty().set(s);
        strideLable.textFillProperty().set(Color.color(1, 0, 0));
        warningsFlowPane.getChildren().add(strideLable);
    }
}
