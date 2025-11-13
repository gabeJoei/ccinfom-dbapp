package com.grp5;

import java.io.IOException;
import java.sql.Connection;

import com.grp5.utils.databaseConnection;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.application.Platform;
import javafx.scene.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

/**
 * Main JavaFX Application Class
 */
public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage primaryStage) throws IOException {

        // 1. Test database connection
        Connection conn = databaseConnection.getConnection();
        if (conn != null) {
            System.out.println("Connected to the database successfully!");
        } else {
            System.out.println("Failed to connect to the database.");

            // Show error pop-up
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Database Connection Error");
            alert.setHeaderText("Failed to connect to the database.");
            alert.setContentText("Please ensure the MySQL server is running and credentials are set.");
            alert.showAndWait();

            // Quit
            Platform.exit();
            return;
        }

        String fxmlFile = "/com/grp5/primary.fxml";
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
        Parent root = loader.load();

    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    // This is the new "main" method. Its ONLY job is to launch the JavaFX app.
    public static void main(String[] args) {
        launch(args);
    }

}