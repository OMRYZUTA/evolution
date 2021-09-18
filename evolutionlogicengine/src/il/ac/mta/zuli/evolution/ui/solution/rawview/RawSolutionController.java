package il.ac.mta.zuli.evolution.ui.solution.rawview;

import il.ac.mta.zuli.evolution.dto.QuintetDTO;
import il.ac.mta.zuli.evolution.dto.TimeTableSolutionDTO;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.util.List;
import java.util.stream.Collectors;

public class RawSolutionController {
    @FXML
    private Label quintetListLabel;

    private final SimpleStringProperty rawQuintets; //private ObservableList<QuintetDTO> rawQuintets;?

    public RawSolutionController() {
        rawQuintets = new SimpleStringProperty("");
    }

    public void setSolution(TimeTableSolutionDTO solution) {
        //sorting quintets by day->hour->class->teacher
        List<QuintetDTO> sortedQuintets = solution.getSolutionQuintets().stream()
                .sorted(QuintetDTO.getRawComparator())
                .collect(Collectors.toList());
        displaySolution(sortedQuintets);
    }

    @FXML
    private void initialize() {
        quintetListLabel.textProperty().bind(rawQuintets);
    }

    private void displaySolution(List<QuintetDTO> quintets) {
        StringBuilder sb = new StringBuilder();

        for (QuintetDTO q : quintets) {
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
