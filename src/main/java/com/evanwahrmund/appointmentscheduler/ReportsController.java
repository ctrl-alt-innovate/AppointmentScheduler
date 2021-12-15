package com.evanwahrmund.appointmentscheduler;



import java.io.IOException;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.LoadException;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class ReportsController {
    @FXML private Button appsByTypeButton;
    @FXML private Button appsByContactButton;
    @FXML private Button appsAndCusByLocButton;

    public void initialize(){
        appsByTypeButton.setOnAction(event -> handleButtonClick(event));
        appsByContactButton.setOnAction(event -> handleButtonClick(event));
        appsAndCusByLocButton.setOnAction(event -> handleButtonClick(event));

    }
    public void handleButtonClick(Event event){
        String name = "FXML/";
        if(event.getSource() == appsByTypeButton)
            name += "apps_by_type_report.fxml";
        else if(event.getSource() == appsByContactButton)
            name += "apps_by_contact_report.fxml";
        else if(event.getSource() == appsAndCusByLocButton)
            name += "apps_and_cus_by_loc_report.fxml";

        try{
            Parent root = FXMLLoader.load(getClass().getResource(name));
            Stage  stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }catch (IOException ex){
            ex.printStackTrace();
        }
    }

}
