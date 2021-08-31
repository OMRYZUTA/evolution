package il.ac.mta.zuli.evolution.ui.endConditions;

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
    }

    public int getStride() {
        int stride=-1;
        try {
             stride = Integer.parseInt(strideField.textProperty().get());

        }
        catch(Throwable e){

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
        boolean result = true;
        if(getStride()<0){
            Label strideLable = new Label();
            strideLable.textProperty().set("stride must be positive number, received: "+strideField.textProperty().get());
            strideLable.textFillProperty().set(Color.color(1, 0, 0));
            warningsFlowPane.getChildren().add(strideLable);
            result=false;
        }
        if(totalGenerationsCheckProperty.get()){
            if(getTotalGenerations()<0){
                Label generationsLable = new Label();
                generationsLable.textProperty().set("total generations must be positive number, received: "+generationTextField.textProperty().get());
                generationsLable.textFillProperty().set(Color.color(1, 0, 0));
                warningsFlowPane.getChildren().add(generationsLable);
                result=false;
            }
        }

        return result;
    }

    private int getTotalGenerations() {
        int generations=-1;
        try {
            generations = Integer.parseInt(generationTextField.textProperty().get());

        }
        catch(Throwable e){

        }
        return generations;

    }
}
