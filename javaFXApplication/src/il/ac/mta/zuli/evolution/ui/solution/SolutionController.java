package il.ac.mta.zuli.evolution.ui.solution;

import il.ac.mta.zuli.evolution.dto.TimeTableDTO;
import il.ac.mta.zuli.evolution.dto.TimeTableSolutionDTO;
import il.ac.mta.zuli.evolution.ui.solution.rawview.RawSolutionController;
import il.ac.mta.zuli.evolution.ui.solution.teacherview.TeacherSolutionController;
import javafx.fxml.FXML;
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


    public void setSolution(TimeTableSolutionDTO solution) {
        teacherSolutionComponentController.setSolution(solution);
        rawSolutionComponentController.setSolution(solution);
    }

    public void setTimeTableSettings(TimeTableDTO timeTable) {
        teacherSolutionComponentController.setTimeTableSettings(timeTable);
    }
}
