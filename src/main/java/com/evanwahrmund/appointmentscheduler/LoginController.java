package com.evanwahrmund.appointmentscheduler;

import com.evanwahrmund.appointmentscheduler.models.User;
import com.evanwahrmund.appointmentscheduler.models.Users;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Locale;
import java.util.ResourceBundle;


public class LoginController {
    /**
     * label to display ZoneId
     */
    @FXML private Label zondIdText;
    /**
     * textfield to collect username
     */
    @FXML private TextField usernameField;
    /**
     * textfield to collect password
     */
    @FXML private TextField passwordField;
    /**
     * button to clear textfields
     */
    @FXML private Button clearButton;
    /**
     * button to attempt login
     */
    @FXML private Button loginButton;
    /**
     * title label for login
     */
    @FXML private Label headerLabel;
    /**
     * label for time zone
     */
    @FXML private Label countryLabel;
    /**
     * label for username textfield
     */
    @FXML private Label usernameLabel;
    /**
     * label for password textfield
     */
    @FXML private Label passwordLabel;
    /**
     * Alert to notify User of incorrect login
     */
    private Alert incorrectLogin;


    /**
     * Sets zondIdText, assigns methods to buttons, and calls localize
     */
    public void initialize()  {
        //TimeZone.setDefault(TimeZone.getTimeZone("America/Chicago"));
        zondIdText.setText(ZoneId.systemDefault().getId());
        clearButton.setOnAction(event -> clearFields());
        loginButton.setOnAction(event -> login());

        localize();


    }

    /**
     * Clears textfields
     */
    private void clearFields(){
        usernameField.clear();
        passwordField.clear();
    }

    /**
     * Attempts a User login. If login credentials are correct, loads main menu of application.
     * If login credentials are incorrect, display error alert. Both successful and unsuccessful login attempt timestamps
     * are appended to file login_activity.txt, saved to root folder of application.
     */
    private void login()  {
        String name = usernameField.getText();
        String password = passwordField.getText();
        boolean incorrect = true;
        for (User u : Users.getUsers()) {
            if ((u.getUsername().equals(name)) && (u.getPassword().equals(password))) {
                    incorrect = false;
                    Stage stage = ((Stage) loginButton.getScene().getWindow());
                    stage.close();
                    Loader.Load("FXML/schedules.fxml", "APPOINTMENT SCHEDULER");
                    try (var pw = new PrintWriter(new FileWriter("login_activity.txt", true))){
                    pw.println("Successful Login Attempt at: " + Util.formatTimestamp(ZonedDateTime.now(ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("UTC"))));
                    }catch (IOException e){
                        e.printStackTrace();
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

    /**
     * Sets the value of all Strings in login page and error message to corresponding values in property files based on User's locale setting
     * English: Login_en_US.properties, French: Login_fr.properties
     */
    private void localize(){
        //Locale current = new Locale("fr");

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

    }

}
