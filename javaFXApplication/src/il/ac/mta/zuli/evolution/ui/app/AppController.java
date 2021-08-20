package il.ac.mta.zuli.evolution.ui.app;

import il.ac.mta.zuli.evolution.dto.DescriptorDTO;
import il.ac.mta.zuli.evolution.engine.Engine;
import il.ac.mta.zuli.evolution.ui.header.HeaderController;
import il.ac.mta.zuli.evolution.ui.timetablesettings.TimetableSettingsController;
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

    public AppController() {
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
    }

    // headerController calls this function
    public void displaySettings(DescriptorDTO descriptor) {
        ttSettingsComponentController.setTimeTable(descriptor.getTimeTable());
//TODO visibility

    }

    public void bestSolutionAction() {
        //only here display rules applied, not before
    }

    public void historyAction() {
    }
}
