package il.ac.mta.zuli.evolution.ui.endConditions;

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
    @FXML
    private void initialize() {
        strideField.focusedProperty().addListener((arg0, oldValue, newValue) -> {
            if (!newValue) { //when focus lost
                if(!strideField.getText().matches("[1-500]")){
                    //set the textField empty
                    strideField.setText("");
                }
            }
        });
    }

}
