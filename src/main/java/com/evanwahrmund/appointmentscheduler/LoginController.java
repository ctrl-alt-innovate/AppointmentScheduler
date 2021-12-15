package com.evanwahrmund.appointmentscheduler;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.time.ZoneId;
import java.util.Locale;


public class LoginController {

    @FXML private Label countryText;

    public void initialize(){
        countryText.setText(ZoneId.systemDefault().getId());

    }
}
