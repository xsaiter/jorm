package jorm;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {
    public static Connection getConnection(){
        try {
            Class.forName("org.postgresql.Driver");

        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Postgresql JDBC not found", e);
        }

        try {
            return DriverManager.getConnection(
                    "jdbc:postgresql://127.0.0.1:5432/venus", "isa",
                    "1q2w3e");

        } catch (SQLException e) {
            throw new RuntimeException("failed to open connection", e);

        }
    }
}
