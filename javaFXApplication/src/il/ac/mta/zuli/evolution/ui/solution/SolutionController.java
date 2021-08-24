package il.ac.mta.zuli.evolution.ui.solution;

import il.ac.mta.zuli.evolution.dto.TimeTableDTO;
import il.ac.mta.zuli.evolution.dto.TimeTableSolutionDTO;
import il.ac.mta.zuli.evolution.ui.solution.teacherview.TeacherSolutionController;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;

public class SolutionController {
    @FXML
    private ScrollPane teacherSolutionComponent;
    @FXML
    private TeacherSolutionController teacherSolutionComponentController;


    public void setSolution(TimeTableSolutionDTO solution) {
        teacherSolutionComponentController.setSolution(solution);
        //rawComponentController.setSolution(solution);
    }

    public void setTimeTableSettings(TimeTableDTO timeTable) {
        teacherSolutionComponentController.setTimeTableSettings(timeTable);
    }
}
