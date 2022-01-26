package totorit.bdd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class BaseDeDonnees {
    private static Connection connection = null;

    public static Connection connection() throws SQLException {
        if (connection == null) {
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:4242/postgres?user=postgres");
        }
        return connection;
    }

}
