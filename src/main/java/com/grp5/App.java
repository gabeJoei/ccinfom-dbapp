package com.grp5;

import com.grp5.utils.databaseConnection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Test Application for Admin or User Selection Screen
 * Tests the initial selection interface
 */
public class App extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        try {
            // Test database connection first
            System.out.println("Testing database connection...");
            if (databaseConnection.testConnection()) {
                System.out.println("✓ Database connected successfully!\n");
            } else {
                System.out.println("⚠️  Database not connected (optional for this screen)\n");
            }
            
            // Load admin_or_user.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/grp5/view/logIn.fxml"));
            Parent root = loader.load();
            
            // Setup scene
            Scene scene = new Scene(root, 1065, 600);
            
            primaryStage.setTitle("Bike Rental System Log-In");
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.show();
            
            System.out.println("✓ Admin or User selection screen loaded successfully!");
            System.out.println("\nInstructions:");
            System.out.println("  - Click 'Admin' button to go to admin interface");
            System.out.println("  - Click 'User' button to go to user interface");
            
        } catch (Exception e) {
            System.err.println("❌ Error loading logIn.fxml:");
            e.printStackTrace();
            
            System.err.println("\nPossible issues:");
            System.err.println("  1. File not found at: /com/grp5/view/logIn.fxml");
            System.err.println("  2. Controller class name mismatch");
            System.err.println("  3. FXML syntax error");
        }
    }
    
    @Override
    public void stop() {
        // Clean up when application closes
        databaseConnection.closeConnection();
        System.out.println("\n✓ Application closed.");
    }
    
    public static void main(String[] args) {
        printHeader();
        launch(args);
    }
    
    /**
     * Print application header
     */
    private static void printHeader() {
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║   ADMIN OR USER SELECTION TEST         ║");
        System.out.println("║                                        ║");
        System.out.println("║   Testing: admin_or_user.fxml          ║");
        System.out.println("║   Controller: AdminOrUserController    ║");
        System.out.println("║                                        ║");
        System.out.println("╚════════════════════════════════════════╝");
        System.out.println();
    }
}