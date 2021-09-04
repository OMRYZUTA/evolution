package il.ac.mta.zuli.evolution.ui.app;

import il.ac.mta.zuli.evolution.dto.DescriptorDTO;
import il.ac.mta.zuli.evolution.dto.TimeTableDTO;
import il.ac.mta.zuli.evolution.dto.TimeTableSolutionDTO;
import il.ac.mta.zuli.evolution.engine.Engine;
import il.ac.mta.zuli.evolution.ui.details.DetailsController;
import il.ac.mta.zuli.evolution.ui.header.HeaderController;
import il.ac.mta.zuli.evolution.ui.runningAlgorithm.RunAlgoController;
import il.ac.mta.zuli.evolution.ui.solution.SolutionController;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
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
    @FXML
    private ScrollPane solutionComponent;
    @FXML
    private SolutionController solutionComponentController;
    @FXML
    private GridPane runAlgoComponent;
    @FXML
    private RunAlgoController runAlgoComponentController;


    // bound to visibility of different body components
    // buttons change its value
    private final SimpleStringProperty currentBodyProperty; //TODO set this inside header, based on button clicks
    private TimeTableSolutionDTO solution;
    private TimeTableDTO timetable;
    private SimpleBooleanProperty doesSolutionExist;
    public AppController() {
        currentBodyProperty = new SimpleStringProperty("none");
        doesSolutionExist = new SimpleBooleanProperty(false);
    }

    public void setEngine(Engine newEngine) {
        this.headerComponentController.setEngine(newEngine);
        this.runAlgoComponentController.setEngine(newEngine);
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.headerComponentController.setPrimaryStage(primaryStage);
    }

    @FXML
    private void initialize() {
        if (headerComponentController != null) {
            headerComponentController.setAppController(this);
        }
        if (runAlgoComponentController != null) {
            runAlgoComponentController.setAppController(this);
        }
        detailsComponent.visibleProperty().bind(Bindings.equal(currentBodyProperty, "details"));
        runAlgoComponent.visibleProperty().bind(Bindings.equal(currentBodyProperty, "runAlgorithm"));
        solutionComponent.visibleProperty().bind(Bindings.equal(currentBodyProperty, "solution"));
    }

    // headerController calls this function from displaySettingsAction
    public void displaySettings(DescriptorDTO descriptor) {
        currentBodyProperty.set("details");
        detailsComponentController.setDescriptor(descriptor);
    }

    public void displaySolution( TimeTableDTO timetable) {
        currentBodyProperty.set("solution");
        solutionComponentController.setSolution(solution);
        solutionComponentController.setTimeTableSettings(timetable);
    }

    public void runAlgorithm() {
        currentBodyProperty.set("runAlgorithm");
    }

    public void updateBestSolution(TimeTableSolutionDTO solution){
        this.solution = solution;
        doesSolutionExist.set(true);
    }

    public TimeTableSolutionDTO getSolution() {
        return solution;
    }


    public SimpleBooleanProperty doesSolutionExistProperty() {
        return doesSolutionExist;
    }
}
