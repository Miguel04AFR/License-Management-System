package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {
    private static final String URL = "jdbc:postgresql://localhost:5432/License";
    private static final String USER = "postgres";
    private static final String PASS = "12345678";

    // Devuelve SIEMPRE una nueva conexión, el llamador debe cerrarla.
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}  