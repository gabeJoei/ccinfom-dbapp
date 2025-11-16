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
            // Test database connection first
            System.out.println("Testing database connection...");
            if (databaseConnection.testConnection()) {
                System.out.println("Database connected successfully!\n");
            } else {
                System.out.println("  Database connection failed (UI loading may still work if no DB access required)\n");
            }
            
            // Load rentBike.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/grp5/view/MainDashBoardUser.fxml"));
            Parent root = loader.load();
            
            // Setup scene
            Scene scene = new Scene(root, 1065, 600);
            
            primaryStage.setTitle("Rent a Bike");
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.show();
            
            System.out.println("✓ rentBike.fxml loaded successfully!");
            System.out.println("\nInstructions:");
            System.out.println("  - Select a branch from the dropdown");
            System.out.println("  - Observe bike availability and details update");
            
        } catch (Exception e) {
            System.err.println(" Error loading rentBike.fxml:");
            e.printStackTrace();
            
            System.err.println("\nPossible issues:");
            System.err.println("  1. File not found at: /com/grp5/view/rentBike.fxml");
            System.err.println("  2. Controller class name mismatch in FXML");
            System.err.println("  3. FXML syntax error");
            System.err.println("  4. Database isn't seeded with branch/bike data");
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
