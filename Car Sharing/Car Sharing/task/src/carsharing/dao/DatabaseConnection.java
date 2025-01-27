package carsharing.dao;

import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final Logger LOGGER = System.getLogger(DatabaseConnection.class.getName());
    private static final String DRIVER = "org.h2.Driver";
    private static final String PATH = "jdbc:h2:C:/mydemo/Car Sharing/Car Sharing/task/src/carsharing/db/";
    private static DatabaseConnection instance;
    private final String connectionName;

    private DatabaseConnection(String dbFilename) {
        this.connectionName = PATH + dbFilename;
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            LOGGER.log(Level.ERROR, "H2 JDBC Driver not found. Ensure the H2 jar is in the classpath.", e);
        }
    }

    public static synchronized DatabaseConnection getInstance(String dbFilename) {
        if (instance == null) {
            instance = new DatabaseConnection(dbFilename);
        }
        return instance;
    }

    public Connection getConnection() {
        try {
            Connection connection = DriverManager.getConnection(connectionName);
            connection.setAutoCommit(true);
            return connection;
        } catch (SQLException e) {
            LOGGER.log(Level.ERROR, "SQL error occurred while connecting to the database.", e);
            throw new RuntimeException(e);
        }
    }

}
