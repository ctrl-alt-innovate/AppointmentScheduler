package com.evanwahrmund.appointmentscheduler;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.time.ZoneId;
import java.util.Locale;


public class LoginController {

    @FXML private Label countryText;
    @FXML private Label exceptionText;
    @FXML private TextField usernameField;
    @FXML private TextField passwordField;
    @FXML private Button clearButton;
    @FXML private Button loginButton;

    public void initialize(){
        countryText.setText(ZoneId.systemDefault().getId());
        clearButton.setOnAction(event -> clearFields());
        loginButton.setOnAction(event -> login());

    }

    private void clearFields(){
        usernameField.clear();
        passwordField.clear();
    }
    private void login(){
        String name = usernameField.getText();
        String password = passwordField.getText();
        for(User u : Users.getUsers()){
            if(u.getUsername().equals(name)){
                if(u.getPassword().equals(password)){
                    Stage stage = ((Stage)loginButton.getScene().getWindow());
                    stage.close();
                    Loader.Load("FXML/schedules.fxml", "APPOINTMENT SCHEDULER");
                }else {
                    exceptionText.setText("Incorrect Username or Password");
                }
            } else {
                exceptionText.setText("Incorrect Username or Password. Please try again.");
            }
        }
    }


}
