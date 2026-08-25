package org.example.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/peerLearner";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "Sanjutech12";

    public static Connection getConnection(){
        try {
            return DriverManager.getConnection(URL , USERNAME , PASSWORD);
        }
        catch (SQLException e){
           throw new RuntimeException("Database connection Failed : "+ e.getMessage() + e);
        }
    }

}
