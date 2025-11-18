package com.grp5.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// Utility class for managing a shared MySQL database connection.
public class databaseConnection {

    /** JDBC URL for the application's MySQL database. */
    private static final String URL = ("jdbc:mysql://localhost:3306/ccinfom");   // Change to 3306 if it doesnt work
    
    /** Fully qualified MySQL JDBC driver class name. */
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";

    /** Shared database connection instance */
    private static Connection connection = null;

     // private constructor
    private databaseConnection() {}

    /**
     * Retrieves a shared database connection. If no connection exists or the
     * current connection is closed, a new one is created.
     * */
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

    // Safely closes the shared database connection if it is open.
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

    //Tests whether a database connection can be successfully established.
    public static boolean testConnection() {
        try {
            Connection conn = getConnection();
            if (conn != null && !conn.isClosed()) {
                System.out.println("Database connection test successful!");
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void main(String[] args) {
        if (!testConnection()) {
            System.out.println("Connection failed! Check your environment variables.");
        }
        closeConnection();
    }
}