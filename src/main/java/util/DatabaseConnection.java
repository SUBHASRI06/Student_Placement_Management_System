package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/placement_db";
    private static final String USER = "root"; // Update if your MySQL username is different
    private static final String PASSWORD = "root123"; // <-- CHANGE THIS to your MySQL password!

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

}