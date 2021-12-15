package com.evanwahrmund.appointmentscheduler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String databaseURL = "jdbc:mysql://wgudb.ucertify.com:3306/WJ07XA5";
    private static final String databaseUsername = "U07XA5";
    private static final String databasePassword = "53689156229";


    public static Connection getConnection() throws SQLException  {
        return DriverManager.getConnection(databaseURL, databaseUsername, databasePassword);
    }

}
