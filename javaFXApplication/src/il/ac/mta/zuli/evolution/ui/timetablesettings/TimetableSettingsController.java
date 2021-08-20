package il.ac.mta.zuli.evolution.ui.timetablesettings;

import il.ac.mta.zuli.evolution.dto.TimeTableDTO;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;

public class TimetableSettingsController {
    ObservableList<String> choices = FXCollections.observableArrayList(
            "Subjects", "Classes", "Teachers", "Rules");

    @FXML
    ChoiceBox<String> ttDisplayDropdown;
    @FXML
    Label selectedDetailsLabel;

    private final SimpleStringProperty selectedDetailsProperty;
    private final TimeTableDTO timeTable;

    public TimetableSettingsController(TimeTableDTO timeTable) {
        this.timeTable = timeTable;
        selectedDetailsProperty = new SimpleStringProperty("");
    }

    @FXML
    private void initialize() {
        ttDisplayDropdown.setItems(choices);
        ttDisplayDropdown.setValue(choices.get(0));
        ttDisplayDropdown.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    switch (newValue) {
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
                });
        selectedDetailsLabel.textProperty().bind(selectedDetailsProperty);
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
