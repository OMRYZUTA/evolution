package il.ac.mta.zuli.evolution.ui.timetablesettings;

import il.ac.mta.zuli.evolution.dto.TimeTableDTO;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;

public class TimetableSettingsController {
    @FXML
    ChoiceBox<String> ttDisplayDropdown;
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
        displayDetails(ttDisplayDropdown.getValue());
        days.set(timetable.getDays());
        hours.set(timetable.getHours());
    }

    @FXML
    private void initialize() {
        ttDisplayDropdown.setItems(choices);
        ttDisplayDropdown.setValue(choices.get(0));
        ttDisplayDropdown.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    displayDetails(newValue);
                });
        selectedDetailsLabel.textProperty().bind(selectedDetailsProperty);
        hoursLabel.textProperty().bind(Bindings.format("%d hours", hours));
        daysLabel.textProperty().bind(Bindings.format("%d days", days));
    }

    private void displayDetails(String type) {
        switch (type) {
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
        selectedDetailsProperty.set("some subjects");
    }

    private void displayClasses() {
        selectedDetailsProperty.set("some classes");
    }

    private void displayTeachers() {
        selectedDetailsProperty.set("some teachers");
    }

    private void displayRules() {
        selectedDetailsProperty.set("some rules");
    }
}
