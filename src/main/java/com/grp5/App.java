package com.grp5;

import com.grp5.utils.databaseConnection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Test Application for Rent Bike Screen
 * Loads the rentBike.fxml to test UI and controller functionality
 */
public class App extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        try {
<<<<<<< HEAD
         Parent root = FXMLLoader.load(getClass().getResource("/com/grp5/view/AdminOrUser.fxml")); // Get the resources :D
            Scene scene = new Scene(root); // Set - up new scene :D

            stage.setScene(scene);
            stage.setTitle("Pick?");
            stage.show();

        } catch (IOException e) {
            e.getStackTrace();
            System.err.println("Tite");
=======

>>>>>>> d70d4ff56e1f76148ded4b9d76813e86613f4053
        }
    }
    
    @Override
    public void stop() {
        // Clean up when application closes
        databaseConnection.closeConnection();
        System.out.println("\n Application closed. Database connection closed.");
    }
    
    public static void main(String[] args) {
        printHeader();
        launch(args);
    }
    
    /**
     * Print application header
     */
    private static void printHeader() {
        System.out.println("         RENT BIKE UI TEST            ");
        System.out.println("   Testing: rentBike.fxml             ");

    }
}
