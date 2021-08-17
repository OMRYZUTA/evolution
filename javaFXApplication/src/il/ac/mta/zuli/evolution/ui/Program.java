package il.ac.mta.zuli.evolution.ui;

import il.ac.mta.zuli.evolution.engine.Engine;
import il.ac.mta.zuli.evolution.engine.TimeTableEngine;
import il.ac.mta.zuli.evolution.ui.mainComponent.MainTTController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;

public class Program extends Application {


    public static void main(String[] args) {
        launch(Program.class);
    }


    @Override
    public void start(Stage primaryStage) throws Exception {

//        CSSFX.start();

        FXMLLoader loader = new FXMLLoader();

        // load main fxml
        URL mainFXML = getClass().getResource("mainComponent/mainTTScene.fxml");
        loader.setLocation(mainFXML);
        VBox root = loader.load();

        // wire up controller
        MainTTController mainController = loader.getController();
        Engine newEngine = new TimeTableEngine(mainController);

        mainController.setPrimaryStage(primaryStage);
        mainController.setEngine(newEngine);

        // set stage
        primaryStage.setTitle("Timetable");
        Scene scene = new Scene(root, 1050, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
