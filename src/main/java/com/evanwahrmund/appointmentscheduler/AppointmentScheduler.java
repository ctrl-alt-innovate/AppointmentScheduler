package com.evanwahrmund.appointmentscheduler;

import java.io.IOException;
import java.time.ZoneId;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TimeZone;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Main Class for Appointment Scheduler Application
 */
public class AppointmentScheduler extends Application {

    /**
     * Launches the Appointment Scheduler Application
     * @param args command line args
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Loads Login page and sets title
     * @param primaryStage initial Stage
     */
    @Override
    public void start(Stage primaryStage){
        //Locale.setDefault(new Locale("fr"));

        Loader.Load("FXML/login.fxml",
                ResourceBundle.getBundle("com.evanwahrmund.appointmentscheduler/Login",Locale.getDefault())
                .getString("title"));

    }
}
