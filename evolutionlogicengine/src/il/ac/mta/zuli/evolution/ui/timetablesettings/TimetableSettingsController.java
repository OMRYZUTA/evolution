package il.ac.mta.zuli.evolution.ui.timetablesettings;

import il.ac.mta.zuli.evolution.dto.*;
import il.ac.mta.zuli.evolution.ui.FXutils;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;

import java.util.Map;
import java.util.Set;


public class TimetableSettingsController {
    @FXML
    ChoiceBox<String> ttDisplayChoiceBox;
    @FXML
    Label selectedDetailsLabel;
    @FXML
    Label hoursLabel;
    @FXML
    Label daysLabel;

    private final SimpleStringProperty selectedDetailsProperty;
    private final SimpleIntegerProperty days;
    private final SimpleIntegerProperty hours;
    private TimeTableDTO timetable;
    private final ObservableList<String> choices = FXCollections.observableArrayList(
            "Subjects", "Classes", "Teachers", "Rules");

    public TimetableSettingsController() {
        selectedDetailsProperty = new SimpleStringProperty("");
        days = new SimpleIntegerProperty();
        hours = new SimpleIntegerProperty();
    }

    public void setTimetable(TimeTableDTO timetable) {
        this.timetable = timetable;
        displayDetails(ttDisplayChoiceBox.getValue());
        days.set(timetable.getDays());
        hours.set(timetable.getHours());
    }

    @FXML
    private void initialize() {
        ttDisplayChoiceBox.setItems(choices);
        ttDisplayChoiceBox.setValue(choices.get(0));
        ttDisplayChoiceBox.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    displayDetails(newValue);
                });
        selectedDetailsLabel.textProperty().bind(selectedDetailsProperty);
        hoursLabel.textProperty().bind(Bindings.format("%d hours", hours));
        daysLabel.textProperty().bind(Bindings.format("%d days", days));
    }

    private void displayDetails(String whatToDisplay) {
        switch (whatToDisplay) {
            case "Subjects":
                displaySubjects();
                break;
            case "Classes":
                displayClasses();
                break;
            case "Teachers":
                displayTeachers();
                break;
            case "Rules":
                displayRules();
                break;
            default:
                // do nothing
        }
    }

    private void displaySubjects() {
        //because we used a TreeMap when building the subjects-map, it's sorted by ID
        String subjectsString = FXutils.myMapToString(this.timetable.getSubjects());
        selectedDetailsProperty.set(subjectsString);
    }

    private void displayClasses() {
        StringBuilder classesSB = new StringBuilder();
        Map<Integer, SchoolClassDTO> schoolClasses = this.timetable.getSchoolClasses();

        for (SchoolClassDTO schoolClass : schoolClasses.values()) {
            classesSB.append(schoolClass + ", requirements: ");

            for (RequirementDTO requirement : schoolClass.getRequirements()) {
                classesSB.append(requirement + " ");
            }
            classesSB.append(System.lineSeparator());
        }

        selectedDetailsProperty.set(classesSB.toString());
    }

    private void displayTeachers() {
        StringBuilder teachersSB = new StringBuilder();
        Map<Integer, TeacherDTO> teachers = this.timetable.getTeachers();

        for (TeacherDTO teacher : teachers.values()) {
            teachersSB.append(teacher + ", teaches subjects: ");
            for (SubjectDTO subject : teacher.getSubjects().values()) {
                teachersSB.append(subject + " ");
            }
            teachersSB.append(System.lineSeparator());
        }

        selectedDetailsProperty.set(teachersSB.toString());
    }

    private void displayRules() {
        StringBuilder rulesSB = new StringBuilder();
        Set<RuleDTO> rules = this.timetable.getRules();

        for (RuleDTO rule : rules) {
            rulesSB.append(rule + System.lineSeparator());
        }

        selectedDetailsProperty.set(rulesSB.toString());
    }
}
