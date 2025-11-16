package com.grp5;

import com.grp5.utils.databaseConnection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Test Application for Bike Reservation Controller
 * Launches the Bike Reservation Management interface
 */
public class TestBikeReservationApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Test database connection first
            System.out.println("Testing database connection...");
            if (!databaseConnection.testConnection()) {
                System.err.println(" Database connection failed!");
                System.err.println("Please ensure:");
                System.err.println("  1. MySQL server is running");
                System.err.println("  2. Database 'ccinfom' exists");
                System.err.println("  3. Username and password are correct");
                System.exit(1);
            }
            System.out.println("✓ Database connected successfully!\n");

            // Load FXML
            // The FXML is now at /com/grp5/controller/bikeReservationView.fxml
            FXMLLoader fxmlLoader = new FXMLLoader(
                    App.class.getResource("/com/grp5/view/bikeReservationView.fxml"));
            Parent root = fxmlLoader.load();

            // Setup stage
            Scene scene = new Scene(root, 1000, 700);

            // Optional: Add CSS styling
            // scene.getStylesheets().add(getClass().getResource("/com/grp5/view/styles.css").toExternalForm());

            primaryStage.setTitle("Bike Reservation Management - Test");
            primaryStage.setScene(scene);
            primaryStage.setResizable(true);
            primaryStage.show();

            System.out.println(" BikeReservation interface loaded successfully!");

        } catch (Exception e) {
            System.err.println(" Error loading BikeReservation interface:");
            e.printStackTrace();
        }
    }

    @Override
    public void stop() {
        // Close database connection on application exit
        databaseConnection.closeConnection();
        System.out.println("\n✓ Application closed. Database connection terminated.");
    }

    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║  BIKE RESERVATION TEST APPLICATION     ║");
        System.out.println("║        Testing Controller              ║");
        System.out.println("╚════════════════════════════════════════╝\n");

        launch(args);
    }
}