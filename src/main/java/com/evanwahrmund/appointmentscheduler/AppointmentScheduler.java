package com.evanwahrmund.appointmentscheduler;

import java.io.IOException;

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
            primaryStage.initStyle(StageStyle.UTILITY);
            ReportVal reportVal = new ReportVal("string", 83);
            reportVal.print();
            Parent root = FXMLLoader.load(this.getClass().getResource("FXML/cal.fxml"));
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("STYLESHEET.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.setTitle("APPOINTMENT SCHEDULER");
            primaryStage.show();
        } catch (IOException ex){
            ex.printStackTrace();
            System.out.println("ERROR: Could not load file");
        }
    }
}
