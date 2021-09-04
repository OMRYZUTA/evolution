package il.ac.mta.zuli.evolution.ui.solution.teacherview;

import il.ac.mta.zuli.evolution.dto.QuintetDTO;
import il.ac.mta.zuli.evolution.dto.TeacherDTO;
import il.ac.mta.zuli.evolution.dto.TimeTableDTO;
import il.ac.mta.zuli.evolution.dto.TimeTableSolutionDTO;
import il.ac.mta.zuli.evolution.ui.solution.DisplaySolutionUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.util.StringConverter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TeacherSolutionController {
    @FXML
    Pane solutionBasePane;
    @FXML
    ChoiceBox<TeacherDTO> teacherChoiceBox;

    private int hours;
    private int days;
    private Map<Integer, TeacherDTO> teachers;
    private Map<TeacherDTO, List<QuintetDTO>> solutionTeacherGroups;
    private TimeTableSolutionDTO solution;
    private ObservableList<TeacherDTO> teacherChoices;

    public void setSolution(TimeTableSolutionDTO solution) {
        this.solution = solution;
    }

    public void setTimeTableSettings(TimeTableDTO timeTable) {
        this.hours = timeTable.getHours();
        this.days = timeTable.getDays();
        this.teachers = timeTable.getTeachers();
        this.solutionTeacherGroups = solution.getSolutionQuintets().stream()
                .collect(Collectors.groupingBy(QuintetDTO::getTeacher));
        this.teacherChoices = FXCollections.observableArrayList(timeTable.getTeachers().values());

        //the following can't happen in initialize() because it causes a NullPointerException
        teacherChoiceBox.setItems(teacherChoices);
        teacherChoiceBox.setValue(teacherChoices.get(0));
    }

    @FXML
    private void initialize() {

        teacherChoiceBox.setConverter(new StringConverter<TeacherDTO>() {
            @Override
            public String toString(TeacherDTO object) {
                return object.getName();
            }

            @Override
            public TeacherDTO fromString(String string) {
                // not used ???
                return null;
            }
        });

        teacherChoiceBox.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    displaySolution(newValue);
                });
    }

    private void displaySolution(TeacherDTO teacher) {
        solutionBasePane.getChildren().clear(); //clears any previous solutions from base

        if (teacher == null) {
            solutionBasePane.getChildren().add(new Label("choice is null"));
            return;
        }

        if (!solutionTeacherGroups.containsKey(teacher)) {
            Label nothingToDisplay = new Label(" This teacher is not scheduled in the timetable.");
            solutionBasePane.getChildren().add(nothingToDisplay);
            return;
        }

        // if we reached this line, we have a teacher-solution
        List<QuintetDTO>[][] teacherSolutionMatrix = buildSolutionMatrix(solutionTeacherGroups.get(teacher));
        GridPane gridPane = DisplaySolutionUtils.createGrid(days, hours);

        for (int d = 0; d < days; d++) {
            for (int h = 0; h < hours; h++) {
                Label label = timeSlotToLabel(teacherSolutionMatrix[h][d]);
                gridPane.add(label, d + 1, h + 1);
                GridPane.setMargin(label, new Insets(5));
            }
        }

        solutionBasePane.getChildren().add(gridPane);
    }

    //TODO make general for different for class and for teacher
    private Label timeSlotToLabel(List<QuintetDTO> quintets) {
        StringBuilder sb = new StringBuilder();
        for (QuintetDTO quintet : quintets) {
            int id1 = quintet.getSchoolClass().getId();
            String s1 = quintet.getSchoolClass().getName();
            int id2 = quintet.getSubject().getId();
            String s2 = quintet.getSubject().getName();
            sb.append(String.format("%d %s, %d %s\n", id1, s1, id2, s2));
        }

        Label label = new Label(sb.toString());
        label.setPadding(new Insets(5));
        return label;
    }

    private List<QuintetDTO>[][] buildSolutionMatrix(Collection<QuintetDTO> quintets) {
        List<QuintetDTO>[][] matrix = (List<QuintetDTO>[][]) new List[hours][days];

        for (int h = 0; h < hours; h++) {
            for (int d = 0; d < days; d++) {

                matrix[h][d] = new ArrayList<>();
            }
        }

        for (QuintetDTO q : quintets) {
            matrix[q.getHour()][q.getDay().getValue() - 1].add(q);
        }

        return matrix;
    }
}
