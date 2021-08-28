package il.ac.mta.zuli.evolution.ui.solution;

import il.ac.mta.zuli.evolution.dto.TimeTableDTO;
import il.ac.mta.zuli.evolution.dto.TimeTableSolutionDTO;
import il.ac.mta.zuli.evolution.ui.solution.rawview.RawSolutionController;
import il.ac.mta.zuli.evolution.ui.solution.teacherview.TeacherSolutionController;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;

public class SolutionController {
    @FXML
    private FlowPane RulePane;
    @FXML
    private ScrollPane teacherSolutionComponent;
    @FXML
    private TeacherSolutionController teacherSolutionComponentController;
    @FXML
    private ScrollPane rawSolutionComponent;
    @FXML
    private RawSolutionController rawSolutionComponentController;
    @FXML
    Label scoreLabel;

    private final SimpleDoubleProperty scoreProperty;

    public SolutionController() {
        scoreProperty = new SimpleDoubleProperty(0);
    }

    public void setSolution(TimeTableSolutionDTO solution) {
        teacherSolutionComponentController.setSolution(solution);
        rawSolutionComponentController.setSolution(solution);
        this.scoreProperty.set(solution.getTotalFitnessScore());
    }

    public void setTimeTableSettings(TimeTableDTO timeTable) {
        teacherSolutionComponentController.setTimeTableSettings(timeTable);
    }

    @FXML
    private void initialize() {
        scoreLabel.textProperty().bind(Bindings.format("Solution Score: %f", scoreProperty));
    }
}
