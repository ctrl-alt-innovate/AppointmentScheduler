package com.evanwahrmund.appointmentscheduler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String databaseURL = "jdbc:mysql://localhost:3306/client_schedule";
    private static final String databaseUsername = "sqlUser";
    private static final String databasePassword = "Passw0rd!";


    public static Connection getConnection() throws SQLException  {
        return DriverManager.getConnection(databaseURL, databaseUsername, databasePassword);
    }

}
