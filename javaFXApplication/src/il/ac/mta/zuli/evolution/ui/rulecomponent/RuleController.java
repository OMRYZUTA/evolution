package il.ac.mta.zuli.evolution.ui.rulecomponent;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class RuleController {

    @FXML
    private Label ruleName;
    @FXML private Label ruleType;

    public RuleController() {

    }

    @FXML
    private void initialize() {
//        ruleName.textProperty().bind(Bindings.concat("<", word, ">"));
//        ruleType.textProperty().bind(count.asString());
    }


    public void setRuleName(String ruleName) {
        this.ruleName.textProperty().set(ruleName);
    }

    public void setRuleType(String ruleType) {
        this.ruleType.textProperty().set(ruleType);
    }

}
