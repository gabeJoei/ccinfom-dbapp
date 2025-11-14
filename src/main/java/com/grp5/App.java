package com.grp5;

import com.grp5.utils.databaseConnection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Test Application for Login
 */
public class App extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        try {
            // Test database connection
            System.out.println("Testing database connection...");
            if (!databaseConnection.testConnection()) {
                System.err.println("❌ Database connection failed!");
            }
            
            Parent root = FXMLLoader.load(getClass().getResource("/com/grp5/view/login.fxml"));
            
            Scene scene = new Scene(root, 600, 400);
            primaryStage.setTitle("Login - Bike Rental System");
            primaryStage.setScene(scene);
            primaryStage.show();
            
            System.out.println("✓ Login screen loaded");
            
        } catch (Exception e) {
            System.err.println("❌ Error loading login.fxml:");
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║         LOGIN SCREEN TEST              ║");
        System.out.println("╚════════════════════════════════════════╝\n");
        
        launch(args);
    }
}