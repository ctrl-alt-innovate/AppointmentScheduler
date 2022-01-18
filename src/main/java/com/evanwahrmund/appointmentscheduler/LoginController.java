package com.evanwahrmund.appointmentscheduler;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
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
    private File loginActivity;


    public void initialize() throws IOException {
        //TimeZone.setDefault(TimeZone.getTimeZone("America/Chicago"));
        countryText.setText(ZoneId.systemDefault().getId());
        clearButton.setOnAction(event -> clearFields());
        loginButton.setOnAction(event -> login());
        //incorrectLogin = new Alert(Alert.AlertType.ERROR);
        //incorrectLogin.setTitle("Error: Login");
        localize();
        //fw = new FileWriter("login_activity.txt", true);
        //pw = new PrintWriter(fw);
        //ZonedDateTime zdt = ZonedDateTime.of(LocalDateTime.now(), ZoneId.of("UTC"));
        //pw.println("Login at " + Util.formatDateTime(zdt));
        //pw.close();

    }

    private void clearFields(){
        usernameField.clear();
        passwordField.clear();
    }
    private void login()  {
        String name = usernameField.getText();
        String password = passwordField.getText();
        boolean incorrect = true;
        for (User u : Users.getUsers()) {
            if (u.getUsername().equals(name)) {
                if (u.getPassword().equals(password)) {
                    incorrect = false;
                    Stage stage = ((Stage) loginButton.getScene().getWindow());
                    stage.close();
                    Loader.Load("FXML/schedules.fxml", "APPOINTMENT SCHEDULER");
                    try (var pw = new PrintWriter(new FileWriter("login_activity.txt", true))){
                    pw.println("Successful Login Attempt at: " + Util.formatTimestamp(ZonedDateTime.now(ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("UTC"))));
                    pw.close();
                    }catch (IOException e){
                        e.printStackTrace();
                    }

                }
            }
        }
        if (incorrect) {
            incorrectLogin.show();
            try(var pw = new PrintWriter(new FileWriter("login_activity.txt", true))){
                pw.println("Unsuccessful Login Attempt at: " + Util.formatTimestamp(ZonedDateTime.now(ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("UTC"))));
            }catch (IOException e){
                e.printStackTrace();
            }


        }
    }
    private void localize(){
        //Locale current = new Locale("fr");

        //Locale current = Locale.getDefault();
        ResourceBundle rb = ResourceBundle.getBundle("com.evanwahrmund.appointmentscheduler/Login", Locale.getDefault());
        ButtonType ok = new ButtonType(rb.getString("ok"), ButtonBar.ButtonData.OK_DONE);
        incorrectLogin = new Alert(Alert.AlertType.ERROR, rb.getString("content"), ok);

        headerLabel.setText(rb.getString("title"));
        countryLabel.setText(rb.getString("location"));
        loginButton.setText(rb.getString("login"));
        clearButton.setText(rb.getString("clear"));
        usernameLabel.setText(rb.getString("username"));
        passwordLabel.setText(rb.getString("password"));
        incorrectLogin.setTitle(rb.getString("error"));
        incorrectLogin.setHeaderText(rb.getString("message"));
        //incorrectLogin.setContentText(rb.getString("content"));
        //incorrectLogin
    }

}
