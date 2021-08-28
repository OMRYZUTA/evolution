package il.ac.mta.zuli.evolution.ui.solution;

import il.ac.mta.zuli.evolution.dto.RuleDTO;
import il.ac.mta.zuli.evolution.dto.TimeTableDTO;
import il.ac.mta.zuli.evolution.dto.TimeTableSolutionDTO;
import il.ac.mta.zuli.evolution.ui.solution.rawview.RawSolutionController;
import il.ac.mta.zuli.evolution.ui.solution.ruletile.RuleController;
import il.ac.mta.zuli.evolution.ui.solution.teacherview.TeacherSolutionController;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.Map;

public class SolutionController {
    @FXML
    private VBox rulePane;
    @FXML
    private ScrollPane teacherSolutionComponent;
    @FXML
    private TeacherSolutionController teacherSolutionComponentController;
    @FXML
    private ScrollPane rawSolutionComponent;
    @FXML
    private RawSolutionController rawSolutionComponentController;
    @FXML
    private Label scoreLabel;

    private final SimpleDoubleProperty scoreProperty;

    public SolutionController() {
        scoreProperty = new SimpleDoubleProperty(0);
    }

    public void setSolution(TimeTableSolutionDTO solution) {
        teacherSolutionComponentController.setSolution(solution);
        rawSolutionComponentController.setSolution(solution);
        scoreProperty.set(solution.getTotalFitnessScore());
        loadRules(solution);
    }

    public void setTimeTableSettings(TimeTableDTO timeTable) {
        teacherSolutionComponentController.setTimeTableSettings(timeTable);
    }

    @FXML
    private void initialize() {
        scoreLabel.textProperty().bind(Bindings.format("Solution Score: %f", scoreProperty));
    }

    private void loadRules(TimeTableSolutionDTO solution) {
        rulePane.getChildren().clear();
        for (Map.Entry<RuleDTO, Double> entry : solution.getFitnessScorePerRule().entrySet()) {
            TilePane ruleTile = createRuleTile(entry.getKey(), entry.getValue());
            rulePane.getChildren().add(ruleTile);
        }
    }

    private TilePane createRuleTile(RuleDTO rule, Double score) {
        try {
            FXMLLoader loader = new FXMLLoader();
            URL mainFXML = getClass().getResource("/il/ac/mta/zuli/evolution/ui/solution/ruletile/ruleComponent.fxml");
            loader.setLocation(mainFXML);
            TilePane ruleTile = loader.load();
            RuleController controller = loader.getController();
            controller.setRule(rule, score);
            return ruleTile;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
