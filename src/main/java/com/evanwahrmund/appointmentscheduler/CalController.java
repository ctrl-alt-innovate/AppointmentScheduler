package com.evanwahrmund.appointmentscheduler;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class CalController {
    @FXML private MenuBar applicationMenuBar;
    @FXML private Menu schedulesMenu;
    @FXML private Menu editInfoMenu;
    @FXML private MenuItem editCus;
    @FXML private MenuItem editApps;
    @FXML private Menu reportsMenu;
    @FXML private MenuItem appsByType;
    @FXML private MenuItem appsAndCusByLoc;
    @FXML private MenuItem contactSchedules;
    @FXML private Menu logout;
    @FXML private GridPane calenderGrid;
    @FXML private Label monthLabel;
    @FXML private Button forwardButton;
    @FXML private Button backwardButton;
    @FXML private RadioButton monthButton;
    @FXML private RadioButton weekButton;


    private static ZonedDateTime current = LocalDateTime.now().withDayOfMonth(1);
    private static final ObservableList<Appointment> currentMonthApps = FXCollections.observableArrayList();

    public void initialize(){
        initializeCalendar(findOffset());
        appsByType.setOnAction(event -> Loader.Load("FXML/apps_by_type_and_month_report.fxml", "APPS BY TYPE AND MONTH REPORT"));
        editCus.setOnAction(event -> Loader.Load("FXML/customers.fxml", "CUSTOMERS"));
        editApps.setOnAction(event -> Loader.Load("FXML/appointments.fxml", "APPOINTMENTS"));
        //appsAndCusByLoc.setOnAction( event -> Loader.Load("FXML/apps_and_cus_by_loc_report.fxml", "APPS AND CUS BY LOCATION REPORT"));
        contactSchedules.setOnAction( event -> Loader.Load("FXML/apps_by_contact_report.fxml", "CONTACT SCHEDULES"));
        updateLabel();
        forwardButton.setOnAction(event -> increaseMonth());
        backwardButton.setOnAction(event -> decreaseMonth());


    }

    private void initializeCalendar(int offset) {
        clearCalDates();
        int minusDays = offset;
        int maxDays = Util.countDaysInMonth(current);
        int currentDay = 1;
        Insets labelInsets = new Insets(5);
        for(int i = 0; i < calenderGrid.getRowCount(); ++i){
            for(int j = offset; j < calenderGrid.getColumnCount(); ++j){
                if(currentDay >= maxDays)
                    return;
                Label label = new Label(String.valueOf(((j+1)+(i*7))-minusDays));
                label.setPadding(labelInsets);
                label.setMinWidth(25);
                TextArea textArea = new TextArea();
                textArea.setPrefHeight(190);
                textArea.setWrapText(true);
                VBox vBox = new VBox(40, label);
                //VBox vbox = new VBox(60, label, but);
                vBox.setPadding(labelInsets);
                vBox.setAlignment(Pos.TOP_CENTER);
                if (checkDate(currentDay)){
                    Appointment app = getApp(currentDay);
                    textArea.appendText(Util.formatTime(app.getStartDateTime().toLocalTime()) + " -- " + app.getTitle());
                    Button but = new Button("...");
                    but.setStyle("-fx-font-size: 8;");
                    but.setMaxWidth(5);
                    but.setMaxHeight(5);
                    Tooltip t = new Tooltip("Enlarge View of Appointments");
                    t.setFont(new Font(10));
                    but.setTooltip(t);
                    vBox.getChildren().add(but);
                }
                HBox hbox = new HBox( vBox, textArea);
                hbox.setPadding(labelInsets);
                calenderGrid.add(hbox, j, i);
                offset = 0;
                currentDay += 1;
            }
        }
        addAppsToSchedule();
    }
    private void addAppsToSchedule(){
        //currentMonthApps.addAll(Appointments.getAppsByMonth(current));
        for(Appointment a: currentMonthApps){
            System.out.println(a.getTitle());
        }
    }
    private void updateLabel(){
        monthLabel.setText(current.toLocalDate().getMonth().toString() + " " + current.toLocalDate().getYear());
    }


    private void increaseMonth(){
        current = current.plusMonths(1);
        currentMonthApps.clear();
        addAppsToSchedule();
        initializeCalendar(findOffset());
        updateLabel();

    }
    private void decreaseMonth(){
        current = current.minusMonths(1);
        currentMonthApps.clear();
        addAppsToSchedule();
        initializeCalendar(findOffset());
        updateLabel();
    }
    private int findOffset(){
        int offset = current.getDayOfWeek().getValue();
        if (offset == 7)
            return offset - 7;
        return offset;
    }
    private void updateCalDates(int offset){

    }
    private void clearCalDates(){
        Node gridlines = calenderGrid.getChildren().get(0);
        calenderGrid.getChildren().clear();
        calenderGrid.getChildren().add(0, gridlines);
    }

    private boolean checkDate(int date){
        for(Appointment app : currentMonthApps){
            if(date == app.getStartDateTime().getDayOfMonth())
                return true;
        }
        return false;
    }
    private Appointment getApp(int date){
        for(Appointment app: currentMonthApps){
            if(date == app.getStartDateTime().getDayOfMonth()){
                return app;
            }

        }
        return null;
    }


    private MenuBar getApplicationMenuBar(){
        return applicationMenuBar;
    }
}
