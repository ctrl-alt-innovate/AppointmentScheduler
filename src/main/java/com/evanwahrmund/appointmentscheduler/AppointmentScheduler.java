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

    @Override
    public void start(Stage primaryStage){
        try{
            //Parent root = FXMLLoader.load(this.getClass().getResource("FXML/customers.fxml"));
            //DatabaseCustomerDao.getInstance();
            //DatabaseAppointmentDao.getInstance();
            //primaryStage.initStyle(StageStyle.UTILITY);
            //ReportVal reportVal = new ReportVal("string", 83);
            //reportVal.print();
            System.out.println(ZoneId.systemDefault());
            Parent root = FXMLLoader.load(this.getClass().getResource("FXML/appointments.fxml"));
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("STYLESHEET.css").toExternalForm());
            primaryStage.setScene(scene);
            //primaryStage.setTitle(ResourceBundle.getBundle("com.evanwahrmund.appointmentscheduler/Login", new Locale("fr"))
                                //.getString("title").toUpperCase());
            //Set<String> ids = ZoneId.getAvailableZoneIds();
            //for(String id: ids)
              //  System.out.println(id);
            primaryStage.show();
        } catch (IOException ex){
            ex.printStackTrace();
            System.out.println("ERROR: Could not load file");
        }
    }
}
