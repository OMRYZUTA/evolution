package il.ac.mta.zuli.evolution.ui.solution.rawView;

import il.ac.mta.zuli.evolution.dto.TimeTableDTO;
import il.ac.mta.zuli.evolution.dto.TimeTableSolutionDTO;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class RawSolutionController {
    @FXML
    Label scoreLabel;

    private int hours;
    private int days;
    private TimeTableSolutionDTO solution;
    private final SimpleDoubleProperty scoreProperty;

    public RawSolutionController() {
        scoreProperty = new SimpleDoubleProperty(0);
    }

    public void setSolution(TimeTableSolutionDTO solution) {
        this.solution = solution;
        scoreProperty.set(solution.getTotalFitnessScore());
    }

    public void setTimeTableSettings(TimeTableDTO timeTable) {
        this.hours = timeTable.getHours();
        this.days = timeTable.getDays();
    }

    @FXML
    private void initialize() {
        scoreLabel.textProperty().bind(Bindings.format("Solution Score: %f", scoreProperty));
    }
}
