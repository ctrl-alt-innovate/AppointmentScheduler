package com.evanwahrmund.appointmentscheduler;

import java.io.IOException;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.LoadException;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.control.ToolBar;
import javafx.stage.Stage;

public class MainMenuController {

    @FXML private Button customerRecordsButton;
    @FXML private Button appointmentsButton;
    @FXML private Button schedulesButton;
    @FXML private Button reportsButton;
    @FXML private Button exitButton;

    public void initialize(){
        customerRecordsButton.setOnAction(event -> handleButtonClick(event));
        appointmentsButton.setOnAction(event -> handleButtonClick(event));
        schedulesButton.setOnAction(event -> handleButtonClick(event));
        reportsButton.setOnAction(event -> handleButtonClick(event));
        exitButton.setOnAction(event -> closeWindow());
    }

    private void handleButtonClick(Event event) {
        String file = "FXML/";
        if (event.getSource() == customerRecordsButton)
            file += "customers.fxml";
        else if (event.getSource() == appointmentsButton)
            file += "appointments.fxml";
        else if(event.getSource() == schedulesButton)
            file += "schedules.fxml";
        else if(event.getSource() == reportsButton)
            file += "reports.fxml";
        try{
            Parent root = FXMLLoader.load(getClass().getResource(file));
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle(file.substring(5,5).toUpperCase() + file.substring(5, file.indexOf(".")));
            stage.show();

        } catch (IOException ex){
            ex.printStackTrace();
        }
    }

    public void closeWindow(){
        ((Stage)exitButton.getScene().getWindow()).close();
    }
}
