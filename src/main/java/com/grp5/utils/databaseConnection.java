package com.grp5.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class databaseConnection {
    private static final String URL = ("jdbc:mysql://localhost:3306/ccinfom");   // Change to 3306 if it doesnt work
    
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";

    private static Connection connection = null;

    private databaseConnection() {} // private constructor

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                String USERNAME = System.getenv("DB_USER");        // environment variable
                String PASSWORD = System.getenv("DB_PASSWORD");    // environment variable

                Class.forName(DRIVER);
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                System.out.println("Database connection established successfully!");
            }
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found!");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Database connection failed!");
            e.printStackTrace();
        }
        return connection;
    }

    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean testConnection() {
        try {
            Connection conn = getConnection();
            if (conn != null && !conn.isClosed()) {
                System.out.println("✓ Database connection test successful!");
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void main(String[] args) {
        System.out.println("Testing database connection...");
        if (!testConnection()) {
            System.out.println("Connection failed! Check your environment variables.");
        }
        closeConnection();
    }
}