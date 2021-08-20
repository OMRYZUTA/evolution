package il.ac.mta.zuli.evolution.ui.enginesettings;

import il.ac.mta.zuli.evolution.dto.EngineSettingsDTO;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;

public class EngineSettingsController {
    @FXML
    ChoiceBox<String> engineDisplayChoiceBox;
    @FXML
    Label selectedDetailsLabel;
    @FXML
    Label populationLabel;

    private final SimpleStringProperty selectedDetailsProperty;
    private final SimpleIntegerProperty population;

    private EngineSettingsDTO engineSettings;
    private final ObservableList<String> choices = FXCollections.observableArrayList(
            "Selection", "Crossover", "Mutation");

    public EngineSettingsController() {
        selectedDetailsProperty = new SimpleStringProperty("");
        population = new SimpleIntegerProperty();
    }

    public void setEngineSettings(EngineSettingsDTO engineSettings) {
        this.engineSettings = engineSettings;
        displayDetails(engineDisplayChoiceBox.getValue());
        population.set(engineSettings.getInitialPopulationSize());
    }

    @FXML
    private void initialize() {
        engineDisplayChoiceBox.setItems(choices);
        engineDisplayChoiceBox.setValue(choices.get(0));
        engineDisplayChoiceBox.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    displayDetails(newValue);
                });
        selectedDetailsLabel.textProperty().bind(selectedDetailsProperty);
        populationLabel.textProperty().bind(Bindings.format("Population: %d", population));
    }

    private void displayDetails(String type) {
        switch (type) {
            case "Selection":
                displaySelection();
                break;
            case "Crossover":
                displayCrossover();
                break;
            case "Mutation":
                displayMutation();
                break;
            default:
                // do nothing
        }
    }

    private void displaySelection() {
        selectedDetailsProperty.set("some selection");
    }

    private void displayCrossover() {
        selectedDetailsProperty.set("some crossover");
    }

    private void displayMutation() {
        selectedDetailsProperty.set("some mutation");
    }
}
