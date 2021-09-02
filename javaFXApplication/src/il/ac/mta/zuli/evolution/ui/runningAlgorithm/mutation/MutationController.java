package il.ac.mta.zuli.evolution.ui.runningAlgorithm.mutation;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

public class MutationController {

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
    private ChoiceBox<?> flippingChoiceBox;

    @FXML
    private TextField sizerTuplesTextField;

}

