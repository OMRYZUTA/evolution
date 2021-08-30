package il.ac.mta.zuli.evolution.ui.endConditions;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;

public class EndConditionsController {
    @FXML
    private DialogPane endConditionsDialogPane;
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

    public EndConditionsController() {
        strideProperty = new SimpleIntegerProperty(0);
        totalGenerationsCheckProperty = new SimpleBooleanProperty(false);
        totalGenerationsProperty = new SimpleIntegerProperty(0);
        scoreCheckProperty = new SimpleBooleanProperty(false);
        scoreProperty = new SimpleDoubleProperty(0f);
        totalMinutesCheckProperty = new SimpleBooleanProperty(false);
        totalMinutesProperty = new SimpleIntegerProperty(0);
    }

    @FXML
    private void initialize() {
        generationTextField.disableProperty().bind(generationCheckbox.selectedProperty().not());
        fitnessTextField.disableProperty().bind(fitnessCheckbox.selectedProperty().not());
        timeTextField.disableProperty().bind(minutesCheckbox.selectedProperty().not());
//        strideField.textProperty().addListener((observable, oldValue, newValue) -> {
//            validateData()
//        });

//        strideField.focusedProperty().addListener((arg0, oldValue, newValue) -> {
//            if (!newValue) { //when focus lost
//                if(!strideField.getText().matches("[1-500]")){
//                    //set the textField empty
//                    strideField.setText("");
//                }
//            }
//        });
    }

    public int getStride() {
        return 30;
//        int stride = Integer.parseInt(strideField.textProperty().get());
//        if (stride <= 0) {
//            throw new ValidationException("Invalid stride value entered, the value must be a positive integer");
//        }
//        return stride;
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
}
