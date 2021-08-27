package il.ac.mta.zuli.evolution.ui.solution.teacherview;

import il.ac.mta.zuli.evolution.dto.QuintetDTO;
import il.ac.mta.zuli.evolution.dto.TeacherDTO;
import il.ac.mta.zuli.evolution.dto.TimeTableDTO;
import il.ac.mta.zuli.evolution.dto.TimeTableSolutionDTO;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.util.StringConverter;
import org.jetbrains.annotations.NotNull;

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
    @FXML
    Label scoreLabel;

    private int hours;
    private int days;
    private Map<Integer, TeacherDTO> teachers;
    private Map<TeacherDTO, List<QuintetDTO>> solutionTeacherGroups;
    private TimeTableSolutionDTO solution;
    private final SimpleDoubleProperty scoreProperty;
    private ObservableList<TeacherDTO> teacherChoices;

    public TeacherSolutionController() {
        scoreProperty = new SimpleDoubleProperty(0);
    }

    public void setSolution(TimeTableSolutionDTO solution) {
        this.solution = solution;
        scoreProperty.set(solution.getTotalFitnessScore());
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
        scoreLabel.textProperty().bind(Bindings.format("Solution Score: %f", scoreProperty));

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
                    if(newValue!=null) {
                        displaySolution(newValue);
                    }
                });
    }

    private void displaySolution(@NotNull TeacherDTO teacher) {
        int teacherSolutionSize = solutionTeacherGroups.get(teacher).size();
        List<QuintetDTO>[][] teacherSolutionMatrix = buildSolutionMatrix(solutionTeacherGroups.get(teacher));
        Label label = new Label();

        solutionBasePane.getChildren().clear(); //clears any previous solutions from base
        GridPane gridPane = createGrid();

        if (teacherSolutionSize > 0) {
            for (int d = 0; d < days; d++) {
                for (int h = 0; h < hours; h++) {
                    label = timeSlotToLabel(teacherSolutionMatrix[h][d]);
                    gridPane.add(label, d, h);
                }
            }

            GridPane.setMargin(label, new Insets(5));
            solutionBasePane.getChildren().add(gridPane);
        } else {
            Label nothingToDisplay = new Label(" This teacher not scheduled in the timetable.");
            solutionBasePane.getChildren().add(nothingToDisplay);
        }
    }

    private GridPane createGrid() {
        GridPane gridPane = new GridPane();
        gridPane.setGridLinesVisible(true);
        gridPane.setAlignment(Pos.CENTER);
        gridPane.prefHeight(Region.USE_COMPUTED_SIZE);
        gridPane.prefWidth(Region.USE_COMPUTED_SIZE);

        int rowCount = hours;
        int columnCount = days;

        RowConstraints rc = new RowConstraints();
        rc.setPercentHeight(100d / rowCount);

        for (int i = 0; i < rowCount; i++) {
            gridPane.getRowConstraints().add(rc);
        }

        ColumnConstraints cc = new ColumnConstraints();
        cc.setPercentWidth(100d / columnCount);

        for (int i = 0; i < columnCount; i++) {
            gridPane.getColumnConstraints().add(cc);
        }

        return gridPane;
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
