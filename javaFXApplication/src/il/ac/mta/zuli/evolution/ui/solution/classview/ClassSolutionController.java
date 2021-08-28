package il.ac.mta.zuli.evolution.ui.solution.classview;

import il.ac.mta.zuli.evolution.dto.QuintetDTO;
import il.ac.mta.zuli.evolution.dto.SchoolClassDTO;
import il.ac.mta.zuli.evolution.dto.TimeTableDTO;
import il.ac.mta.zuli.evolution.dto.TimeTableSolutionDTO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.util.StringConverter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ClassSolutionController {
    @FXML
    Pane solutionBasePane;
    @FXML
    ChoiceBox<SchoolClassDTO> classChoiceBox;

    private int hours;
    private int days;
    private Map<Integer, SchoolClassDTO> classes;
    private Map<SchoolClassDTO, List<QuintetDTO>> solutionClassGroups;
    private TimeTableSolutionDTO solution;
    private ObservableList<SchoolClassDTO> classChoices;

    public void setSolution(TimeTableSolutionDTO solution) {
        this.solution = solution;
    }

    public void setTimeTableSettings(TimeTableDTO timeTable) {
        this.hours = timeTable.getHours();
        this.days = timeTable.getDays();
        this.classes = timeTable.getSchoolClasses();
        this.solutionClassGroups = solution.getSolutionQuintets().stream()
                .collect(Collectors.groupingBy(QuintetDTO::getSchoolClass));
        this.classChoices = FXCollections.observableArrayList(timeTable.getSchoolClasses().values());

        //the following can't happen in initialize() because it causes a NullPointerException
        classChoiceBox.setItems(classChoices);
        classChoiceBox.setValue(classChoices.get(0));
    }

    @FXML
    private void initialize() {

        classChoiceBox.setConverter(new StringConverter<SchoolClassDTO>() {
            @Override
            public String toString(SchoolClassDTO object) {
                return object.getName();
            }

            @Override
            public SchoolClassDTO fromString(String string) {
                // not used ???
                return null;
            }
        });

        classChoiceBox.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    displaySolution(newValue);
                });
    }

    private void displaySolution(SchoolClassDTO schoolClass) {
        solutionBasePane.getChildren().clear(); //clears any previous solutions from base

        if (schoolClass == null) {
            solutionBasePane.getChildren().add(new Label("choice is null"));
            return;
        }

        if (!solutionClassGroups.containsKey(schoolClass)) {
            Label nothingToDisplay = new Label(" This class is not scheduled in the timetable.");
            solutionBasePane.getChildren().add(nothingToDisplay);
            return;
        }

        // if we reached this line, we have a class-solution
        List<QuintetDTO>[][] classSolutionMatrix = buildSolutionMatrix(solutionClassGroups.get(schoolClass));
        GridPane gridPane = createGrid();

        for (int d = 0; d < days; d++) {
            for (int h = 0; h < hours; h++) {
                Label label = timeSlotToLabel(classSolutionMatrix[h][d]);
                gridPane.add(label, d, h);
                GridPane.setMargin(label, new Insets(5));
            }
        }

        solutionBasePane.getChildren().add(gridPane);
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
//            <teacher id, teacher name, subject id, subject name>
            int id1 = quintet.getTeacherID();
            String s1 = quintet.getTeacher().getName();
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
