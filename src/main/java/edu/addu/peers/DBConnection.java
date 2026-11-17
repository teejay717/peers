package edu.addu.peers;

import java.sql.*;

public class DBConnection {
    private static final String URL = System.getenv().getOrDefault("DB_URL", "jdbc:mysql://localhost:3306/peers");
    private static final String USER = System.getenv().getOrDefault("DB_USER", "root");
    private static final String PASSWORD = System.getenv().getOrDefault("DB_PASSWORD", "root");

    public static Connection getConnection() throws SQLException {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.err.println("\n[Database Connection Error]");
            System.err.println("Could not connect to MySQL at: " + URL);
            System.err.println("Connecting as user: '" + USER + "'");
            System.err.println("Tip: Check that MySQL is running, the 'peers' database exists (run database.sql), and DB_USER/DB_PASSWORD in .vscode/launch.json match your local MySQL credentials.\n");
            throw e;
        }
    }

    public static void main(String[] args) {
        try (Connection connection = getConnection()) {
            if (connection != null) {
                System.out.println("Connected to the database!");
            } else {
                System.out.println("Failed to make connection!");
            }
        } catch (SQLException e) {
            System.err.println("SQL Exception: " + e.getMessage());
        }
    }
}
