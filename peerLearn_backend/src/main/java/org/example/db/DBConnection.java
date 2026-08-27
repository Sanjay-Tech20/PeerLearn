package org.example.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static String getEnv(String key, String defaultValue) {
        String value = System.getenv(key);
        return (value != null) ? value : defaultValue;
    }

    private static final String HOST = getEnv("DB_HOST", "localhost");
    private static final String PORT = getEnv("DB_PORT", "3306");
    private static final String DB_NAME = getEnv("DB_NAME", "peerLearner");
    private static final String USERNAME = getEnv("DB_USER", "root");
    private static final String PASSWORD = getEnv("DB_PASSWORD", "Sanjutech12");

    private static final String URL = "jdbc:mysql://" + HOST + ":" + PORT + "/" + DB_NAME;

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException("Database connection failed: " + e.getMessage(), e);
        }
    }
}