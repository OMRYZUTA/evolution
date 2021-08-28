package il.ac.mta.zuli.evolution.ui.solution.ruletile;

import il.ac.mta.zuli.evolution.dto.RuleDTO;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class RuleController {
    @FXML
    Label scoreLabel;
    @FXML
    Label nameLabel;
    @FXML
    Label typeLabel;
    @FXML
    Label paramLabel;

    private final SimpleDoubleProperty scoreProperty;
    private final SimpleStringProperty nameProperty;
    private final SimpleStringProperty typeProperty;
    private final SimpleStringProperty paramProperty;

    public RuleController() {
        scoreProperty = new SimpleDoubleProperty(0);
        nameProperty = new SimpleStringProperty("");
        typeProperty = new SimpleStringProperty("");
        paramProperty = new SimpleStringProperty("");
    }

    public void setRule(RuleDTO rule, double score) {
        this.scoreProperty.set(score);
        this.nameProperty.set(rule.getName());
        this.typeProperty.set(rule.getType());
        this.paramProperty.set(rule.getParams());
    }

    @FXML
    private void initialize() {
        scoreLabel.textProperty().bind(Bindings.format("Score: %.1f", scoreProperty));
        nameLabel.textProperty().bind(Bindings.format("Name: %s", nameProperty));
        typeLabel.textProperty().bind(Bindings.format("Type: %s", typeProperty));
        paramLabel.textProperty().bind(Bindings.format("Parameters: %s", paramProperty));
        paramLabel.visibleProperty().bind(Bindings.isNotEmpty(paramProperty));
    }
}
