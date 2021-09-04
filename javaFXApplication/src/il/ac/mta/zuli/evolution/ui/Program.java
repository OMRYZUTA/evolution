package il.ac.mta.zuli.evolution.ui;

import il.ac.mta.zuli.evolution.engine.Engine;
import il.ac.mta.zuli.evolution.engine.TimeTableEngine;
import il.ac.mta.zuli.evolution.ui.app.AppController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;
import org.fxmisc.cssfx.CSSFX;

import java.net.URL;

public class Program extends Application {

    public static void main(String[] args) {
        launch(Program.class);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        CSSFX.start();

        FXMLLoader loader = new FXMLLoader();

        // load main fxml
        URL mainFXML = getClass().getResource("/il/ac/mta/zuli/evolution/ui/app/app.fxml");
        loader.setLocation(mainFXML);
        ScrollPane root = loader.load();

        // wire up controller
        AppController appController = loader.getController();
        Engine newEngine = new TimeTableEngine();

        appController.setPrimaryStage(primaryStage);
        appController.setEngine(newEngine);

        // set stage
        primaryStage.setTitle("Timetable");
        //TODO set size
        Scene scene = new Scene(root, 1050, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
