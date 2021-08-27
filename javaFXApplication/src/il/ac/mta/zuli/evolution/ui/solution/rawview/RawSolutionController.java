package il.ac.mta.zuli.evolution.ui.solution.rawview;

import il.ac.mta.zuli.evolution.dto.QuintetDTO;
import il.ac.mta.zuli.evolution.dto.TimeTableSolutionDTO;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.util.List;
import java.util.stream.Collectors;

public class RawSolutionController {
    @FXML
    private Label quintetListLabel;
    @FXML
    private Label scoreLabel;

    private TimeTableSolutionDTO solution;
    List<QuintetDTO> sortedQuintets;
    private final SimpleDoubleProperty scoreProperty;
    //TODO continue from here
    private final SimpleStringProperty rawQuintets; //private ObservableList<QuintetDTO> rawQuintets;?

    public RawSolutionController() {
        scoreProperty = new SimpleDoubleProperty(0);
        rawQuintets = new SimpleStringProperty("");
    }

    public void setSolution(TimeTableSolutionDTO solution) {
        this.solution = solution;
        this.scoreProperty.set(solution.getTotalFitnessScore());
        //sorting quintets by day->hour->class->teacher
        this.sortedQuintets = this.solution.getSolutionQuintets().stream()
                .sorted(QuintetDTO.getRawComparator())
                .collect(Collectors.toList());
    }

    @FXML
    private void initialize() {
        scoreLabel.textProperty().bind(Bindings.format("Solution Score: %f", scoreProperty));
        quintetListLabel.textProperty().bind(rawQuintets);
    }

    private void displaySolution() {
        //where do we call displaySolution
        StringBuilder sb = new StringBuilder();

        for (QuintetDTO q : sortedQuintets) {
            sb.append(String.format("<%d, %d, %d, %d, %d>%n",
                    q.getDay().getValue(),
                    q.getHour(),
                    q.getSchoolClass().getId(),
                    q.getTeacher().getId(),
                    q.getSubject().getId()));
        }

        rawQuintets.set(sb.toString());
    }
}
