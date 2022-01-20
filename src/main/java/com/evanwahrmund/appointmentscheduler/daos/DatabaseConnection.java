package com.evanwahrmund.appointmentscheduler.daos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Utility class for making database connection
 */
public class DatabaseConnection {
    /**
     * database url with name
     */
    private static final String databaseURL = "jdbc:mysql://localhost:3306/client_schedule";
    /**
     * database username
     */
    private static final String databaseUsername = "sqlUser";
    /**
     * database password
     */
    private static final String databasePassword = "Passw0rd!";

    /**
     * Gets database connection
     * @return Connection if successful
     * @throws SQLException if connection unsuccessful
     */
    public static Connection getConnection() throws SQLException  {
        return DriverManager.getConnection(databaseURL, databaseUsername, databasePassword);
    }

}
