package il.ac.mta.zuli.evolution.ui.app;

import il.ac.mta.zuli.evolution.dto.DescriptorDTO;
import il.ac.mta.zuli.evolution.engine.Engine;
import il.ac.mta.zuli.evolution.ui.details.DetailsController;
import il.ac.mta.zuli.evolution.ui.header.HeaderController;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;

public class AppController {
    @FXML
    private ScrollPane headerComponent;
    @FXML
    private HeaderController headerComponentController;
    @FXML
    private ScrollPane detailsComponent;
    @FXML
    private DetailsController detailsComponentController;

    // bound to visibility of different body components
    // buttons change its value
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

        detailsComponent.visibleProperty().bind(Bindings.equal(currentBodyProperty, "details"));
    }

    // headerController calls this function from displaySettingsAction
    public void displaySettings(DescriptorDTO descriptor) {
        detailsComponentController.setDescriptor(descriptor);
        currentBodyProperty.set("details");
    }

    public void bestSolutionAction() {
        //only here display rules applied, not before
    }

    public void historyAction() {
    }
}
