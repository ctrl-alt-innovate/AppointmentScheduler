package com.evanwahrmund.appointmentscheduler;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Time;
import java.time.ZoneId;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.TimeZone;


public class LoginController {

    @FXML private Label countryText;
    @FXML private Label exceptionText;
    @FXML private TextField usernameField;
    @FXML private TextField passwordField;
    @FXML private Button clearButton;
    @FXML private Button loginButton;

    @FXML private Label headerLabel;
    @FXML private Label countryLabel;
    @FXML private Label usernameLabel;
    @FXML private Label passwordLabel;

    private Alert incorrectLogin;


    public void initialize(){
        //TimeZone.setDefault(TimeZone.getTimeZone("America/Chicago"));
        countryText.setText(ZoneId.systemDefault().getId());
        clearButton.setOnAction(event -> clearFields());
        loginButton.setOnAction(event -> login());
        incorrectLogin = new Alert(Alert.AlertType.ERROR, "Incorrect Username or Password. ");
        //incorrectLogin.setTitle("Error: Login");
        localize();
    }

    private void clearFields(){
        usernameField.clear();
        passwordField.clear();
    }
    private void login(){
        String name = usernameField.getText();
        String password = passwordField.getText();
        boolean incorrect = true;
        for(User u : Users.getUsers()){
            if(u.getUsername().equals(name)){
                if(u.getPassword().equals(password)){
                    incorrect = false;
                    Stage stage = ((Stage)loginButton.getScene().getWindow());
                    stage.close();
                    Loader.Load("FXML/schedules.fxml", "APPOINTMENT SCHEDULER");
                }else {
                    //exceptionText.setText("Incorrect Username or Password");

                }
            } else {
                //exceptionText.setText("Incorrect Username or Password. Please try again.");

            }
            if(incorrect)
                incorrectLogin.show();
        }
    }
    private void localize(){
        Locale current = new Locale("fr");
        //Locale current = Locale.getDefault();
        ResourceBundle rb = ResourceBundle.getBundle("com.evanwahrmund.appointmentscheduler/Login", current);
        headerLabel.setText(rb.getString("title"));
        countryLabel.setText(rb.getString("location"));
        loginButton.setText(rb.getString("login"));
        clearButton.setText(rb.getString("clear"));
        usernameLabel.setText(rb.getString("username"));
        passwordLabel.setText(rb.getString("password"));
        incorrectLogin.setTitle(rb.getString("error"));
        incorrectLogin.setHeaderText(rb.getString("message"));
        incorrectLogin.setContentText(rb.getString("content"));
    }

}
