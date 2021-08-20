package il.ac.mta.zuli.evolution.ui.app;

import il.ac.mta.zuli.evolution.dto.DescriptorDTO;
import il.ac.mta.zuli.evolution.engine.Engine;
import il.ac.mta.zuli.evolution.ui.header.HeaderController;
import il.ac.mta.zuli.evolution.ui.timetablesettings.TimetableSettingsController;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class AppController {
    @FXML
    private ScrollPane headerComponent;
    @FXML
    private HeaderController headerComponentController;
    @FXML
    private StackPane centerStackPane;
    @FXML
    private ScrollPane ttSettingsComponent;
    @FXML
    private TimetableSettingsController ttSettingsComponentController;

    private final SimpleStringProperty currentBodyProperty;

    public AppController() {
        currentBodyProperty = new SimpleStringProperty("none");
    }

    public void setEngine(Engine newEngine) {
        this.headerComponentController.setEngine(newEngine);
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.headerComponentController.setPrimaryStage(primaryStage);
    }

    @FXML
    private void initialize() {
        if (headerComponentController != null) {
            headerComponentController.setAppController(this);
        }

        ttSettingsComponent.visibleProperty().bind(Bindings.equal(currentBodyProperty, "ttSettings"));
    }

    // headerController calls this function
    public void displaySettings(DescriptorDTO descriptor) {
        ttSettingsComponentController.setTimetable(descriptor.getTimeTable());
        currentBodyProperty.set("ttSettings");
    }

    public void bestSolutionAction() {
        //only here display rules applied, not before
    }

    public void historyAction() {
    }
}
