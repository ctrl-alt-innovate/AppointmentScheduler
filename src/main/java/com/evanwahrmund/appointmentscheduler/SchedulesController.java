package com.evanwahrmund.appointmentscheduler;


import java.time.*;
import java.time.temporal.TemporalAccessor;
import java.util.Locale;
import java.util.TimeZone;

import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import javax.swing.*;

public class SchedulesController {

    @FXML
    private TableView<Appointment> schedulesTable;
    @FXML
    private TableColumn<Appointment, Integer> idCol;
    @FXML
    private TableColumn<Appointment, String> titleCol;
    @FXML
    private TableColumn<Appointment, String> typeCol;
    @FXML
    private TableColumn<Appointment, String> descriptionCol;
    @FXML
    private TableColumn<Appointment, String> locationCol;
    @FXML
    private TableColumn<Appointment, Contact> contactCol;
    @FXML
    private TableColumn<Appointment, ZonedDateTime> startDateTimeCol;
    @FXML
    private TableColumn<Appointment, ZonedDateTime> endDateTimeCol;
    @FXML
    private TableColumn<Appointment, Customer> customerCol;

    @FXML
    private ComboBox<TemporalAccessor> choiceComboBox;
    @FXML
    private Label choiceLabel;
    @FXML
    private ToggleGroup schedulesGroup;
    @FXML
    private RadioButton weekRadioButton;
    @FXML
    private RadioButton monthRadioButton;
    @FXML
    private RadioButton allRadioButton;

    @FXML
    private Button modifyButton;
    @FXML
    private Button saveButton;

    @FXML
    private DatePicker startDatePicker;
    @FXML
    private DatePicker endDatePicker;
    @FXML
    private ComboBox<LocalDateTime> startTimeComboBox;
    @FXML
    private ComboBox<LocalDateTime> endTimeComboBox;

    @FXML
    private MenuBar applicationMenuBar;
    @FXML
    private Menu schedulesMenu;
    @FXML
    private Menu editInfoMenu;
    @FXML
    private MenuItem editCus;
    @FXML
    private MenuItem editApps;
    @FXML
    private Menu reportsMenu;
    @FXML
    private MenuItem appsByType;
    @FXML
    private MenuItem appsAndCusByLoc;
    @FXML
    private MenuItem contactSchedules;
    @FXML
    private Menu logoutMenu;
    @FXML
    private MenuItem logout;
    private ObservableList<TemporalAccessor> options = FXCollections.observableArrayList();
    private ObservableList<YearMonth> monthOptions;
    private LocalDate current = LocalDateTime.now().toLocalDate();

    @FXML
    public void initialize() {

        initializeTable();
        //initializeWeekComboBox();

        setMonthOptions();
        choiceComboBox.setItems(options);
        schedulesTable.setItems(null);
        modifyButton.setOnAction(event -> modifyAppTime());
        //saveButton.setOnAction(event -> updateAppTime());
        EventHandler<ActionEvent> eventHandler = (event) -> {
            System.out.println(event.getSource());
            if(event.getSource() == startTimeComboBox){
                    LocalDateTime ldt = startTimeComboBox.getSelectionModel().getSelectedItem();
                    System.out.println("ldt: " + ldt);
                    startTimeComboBox.setValue(ldt);
                    LocalDate ld = ldt.toLocalDate();
                    System.out.println(ld);
                    // startTimeComboBox.setValue(startTimeComboBox.getSelectionModel().getSelectedItem());
                    changeDay(startTimeComboBox.getSelectionModel().getSelectedItem());

            }else if(event.getSource() == startDatePicker){
                initializeComboBoxes();
            }
        };
        startTimeComboBox.valueProperty().addListener((obs, old, newVal) -> {
            if(newVal != null && newVal.toLocalDate().isAfter(startDatePicker.getValue())){
                startDatePicker.setValue(newVal.toLocalDate());
                for(LocalDateTime ldt: startTimeComboBox.getItems()){
                    ldt.minusDays(1);
                }
            }
        });
        appsByType.setOnAction(event -> Loader.Load("FXML/apps_by_type_and_month_report.fxml", "APPS BY TYPE AND MONTH REPORT"));
        editCus.setOnAction(event -> Loader.Load("FXML/customers.fxml", "CUSTOMERS"));
        editApps.setOnAction(event -> Loader.Load("FXML/appointments.fxml", "APPOINTMENTS"));
        //appsAndCusByLoc.setOnAction( event -> Loader.Load("FXML/apps_and_cus_by_loc_report.fxml", "APPS AND CUS BY LOCATION REPORT"));
        contactSchedules.setOnAction(event -> Loader.Load("FXML/apps_by_contact_report.fxml", "CONTACT SCHEDULES"));
        logout.setOnAction(actionEvent -> {
            Stage stage = ((Stage)saveButton.getScene().getWindow());
            stage.close();
            Loader.Load("FXML/login.fxml", "LOGIN");
        });
        weekRadioButton.setOnAction(event -> {
            choiceLabel.setText("Week");
            setWeekOptions();
            choiceComboBox.setItems(options);
            schedulesTable.setItems(null);
        });
        monthRadioButton.setOnAction(event -> {
            choiceLabel.setText("Month");
            setMonthOptions();
            choiceComboBox.setItems(options);
            schedulesTable.setItems(null);
        });
        allRadioButton.setOnAction(event -> {
            choiceLabel.setText("Viewing All Appointments");
            schedulesTable.setItems(Appointments.getAppointments());
            choiceComboBox.setItems(null);

        });
        monthRadioButton.setSelected(true);
        choiceComboBox.setOnAction(event -> {
            if (choiceComboBox.getValue() != null)
                schedulesTable.setItems(setMonthlySchedule());
        });
        choiceComboBox.setConverter(new StringConverter<TemporalAccessor>() {
            @Override
            public String toString(TemporalAccessor temporalAccessor) {
                if (temporalAccessor != null) {
                    return weekRadioButton.isSelected() ?
                            "Week: " + temporalAccessor.toString().substring(6) + "-" +
                                    temporalAccessor.toString().substring(0, 4) :
                            "Month: " + ((YearMonth) temporalAccessor).getMonth().toString() + " " +
                                    temporalAccessor.toString().substring(0, 4);
                }
                return null;
            }

            @Override
            public TemporalAccessor fromString(String s) {
                return null;
            }
        });
        //startDatePicker.setOnAction(event -> {
            //endDatePicker.setValue(startDatePicker.getValue());
            //changed = false;
            //LocalDateTime ldt = startTimeComboBox.getSelectionModel().getSelectedItem();
            //System.out.println(ldt.toString());
            //changeDay(ldt);
            //current = startDatePicker.getValue();
            //current = startDatePicker.getValue();
            //initializeComboBoxes();
            //changeDay(ldt);
        //});
        startDatePicker.setOnAction(event -> initializeComboBoxes());
        startDatePicker.setValue(current);
        initializeComboBoxes();

        Platform.runLater(() -> {
            notifyUpcomingAppointment();
        });
        /*startTimeComboBox.setOnAction(actionEvent -> {
            LocalDate today = current;
            LocalDate next = current.plusDays(1);
            int index = startTimeComboBox.getItems().indexOf(startTimeComboBox.getValue());
            //SortedList<LocalTime> sorted = startTimeComboBox.getItems().sorted();
            int indexOfNextDay = startTimeComboBox.getItems().indexOf(LocalTime.of(0,0));
            System.out.println(index + " " + indexOfNextDay);
            if(index >= indexOfNextDay){
                current = current.minusDays(1);
                startDatePicker.setValue(next);
            }  else  {
                System.out.println(today);
                startDatePicker.setValue(today);
            }



        });*//*
        startTimeComboBox.setOnAction(event -> {
            if(startTimeComboBox.getValue().toLocalDate().isAfter(startDatePicker.getValue())) {
                current = startDatePicker.getValue();
                startDatePicker.setValue(startDatePicker.getValue().plusDays(1));
            }
        });*/
        /*startTimeComboBox.setConverter(new StringConverter<LocalDateTime>() {
            @Override
            public String toString(LocalDateTime localDateTime) {
                if(localDateTime != null)
                    return localDateTime.toLocalTime().toString();
                return null;
            }

            @Override
            public LocalDateTime fromString(String s) {
                return null;
            }
        });*/



        //populateTimeComboBoxes();
    }

    private void notifyUpcomingAppointment() {
        Alert notification = new Alert(Alert.AlertType.INFORMATION);
        StringBuilder sb = new StringBuilder();
        for(Appointment a: Appointments.getAppointments()) {
            if(checkIfUpcoming(a)){
                sb.append("UPcoming app: " + a.getTitle() + "\n");
            }
        }
        if(sb.length() == 0){
            notification.setContentText("No Upcoming Apps.");
        }else{
            notification.setContentText(sb.toString());
        }
        notification.show();
    }
    private Boolean checkIfUpcoming(Appointment app){
        LocalDateTime now = LocalDateTime.now();
        ZonedDateTime zdt = ZonedDateTime.of(now, ZoneId.systemDefault());
        ZonedDateTime utc = zdt.withZoneSameInstant(ZoneId.of("UTC"));
        if((now.isAfter(app.getStartDateTime().toLocalDateTime())) && (now.isBefore(app.getEndDateTime().toLocalDateTime()))){
            return true;
        } else {
            return false;
        }

    }

    private ObservableList<Appointment> setMonthlySchedule() {
        ObservableList<Appointment> currentSchedule = FXCollections.observableArrayList();
        TemporalAccessor month = choiceComboBox.getSelectionModel().getSelectedItem();
        if (monthRadioButton.isSelected()) {
            int monthVal = ((YearMonth) month).getMonthValue();
            int yearVal = ((YearMonth) month).getYear();
            currentSchedule = AppointmentDatabaseDao.getInstance().getAppsByMonth(monthVal, yearVal);
        } else {
            LocalDate date = ((LocalDate) month);
            LocalDate end = date.plusDays(7);
            currentSchedule = AppointmentDatabaseDao.getInstance().getAppsByWeek(date.toString(), end.toString());
        }
        return currentSchedule;
    }

    private void modifyAppTime() {
        Appointment appointment = schedulesTable.getSelectionModel().getSelectedItem();

        startDatePicker.setValue(appointment.getStartDateTime().toLocalDate());

        endDatePicker.setValue(appointment.getEndDateTime().toLocalDate());
        startTimeComboBox.setValue(appointment.getStartDateTime().withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime());

        endTimeComboBox.setValue(appointment.getEndDateTime().withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime());


    }
/*
    private void updateAppTime() {
        Appointment appointment = schedulesTable.getSelectionModel().getSelectedItem();
        ZonedDateTime start = LocalDateTime.of(Util.stringToDate(startDateTextField.getText()),
                Util.stringToTime(startTimeTextField.getText()));
        ZonedDateTime end = LocalDateTime.of(Util.stringToDate(endDateTextField.getText()),
                Util.stringToTime(endTimeTextField.getText()));
        appointment.setStartDateTime(start);
        appointment.setEndDateTime(end);
        AppointmentDatabaseDao.getInstance().updateAppTime(appointment);
        schedulesTable.refresh();
    }*/

    private void setMonthOptions() {
        options.clear();
        for (Appointment app : Appointments.getAppointments()) {
            Month month = app.getStartDateTime().getMonth();
            int year = app.getStartDateTime().getYear();
            YearMonth yearMonth = YearMonth.of(year, month);
            if (!options.contains(yearMonth)) {
                options.add(yearMonth);
            }

        }
    }

    private void setWeekOptions() {
        options.clear();
        for (Appointment app : Appointments.getAppointments()) {
            LocalDate date = app.getStartDateTime().toLocalDate();
            int day = date.getDayOfWeek().getValue();
            int diff = 0;
            while (day > 1) {
                --day;
                ++diff;
            }
            date = date.minusDays(diff);
            if (!options.contains(date)) {
                options.add(date);
            }
        }
    }

    private void initializeTable() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        locationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        contactCol.setCellValueFactory(cell -> new ReadOnlyObjectWrapper(cell.getValue().getContact().getName()));
        startDateTimeCol.setCellValueFactory(cell -> new ReadOnlyObjectWrapper(Util.formatDateTime(cell.getValue().getStartDateTime()
                .withZoneSameInstant(ZoneId.systemDefault())))); // add system defaults
        endDateTimeCol.setCellValueFactory(cell -> new ReadOnlyObjectWrapper(Util.formatDateTime(cell.getValue().getEndDateTime()
                .withZoneSameInstant(ZoneId.systemDefault())))); //add system defaults
        customerCol.setCellValueFactory(cell -> new ReadOnlyObjectWrapper(cell.getValue().getCustomer().getId()));

    }
    //
    private void initializeComboBoxes(){
        ObservableList<LocalDateTime> options = FXCollections.observableArrayList();
        LocalDate local = startDatePicker.getValue();
        System.out.println(local);
        current = local;
        System.out.println("current val: " + current);
        ZonedDateTime zdt = ZonedDateTime.of(LocalDateTime.of(local, LocalTime.of(8,0)), ZoneId.of("America/New_York") );

        zdt = zdt.withZoneSameInstant(ZoneId.of("UTC"));
        ObservableList<LocalDate> localDates = FXCollections.observableArrayList();


        int count = 0;
        while (count <= 24){
            LocalDateTime option = zdt.plusMinutes(count * 30).toLocalDateTime();
            System.out.println(option.toLocalDate());
            options.add(option);
            count++;
        }
        startTimeComboBox.setItems(options);
        endTimeComboBox.setItems(options);
        //startTimeComboBox.setValue(null);
    }
    private void changeDay(LocalDateTime ldt) {
        /*if (ldt == null) {
            initializeComboBoxes();
            return;
        }*/System.out.println(ldt);
            if (ldt.toLocalDate().isAfter(current)) {
                current = current.plusDays(1);
                startDatePicker.setValue(current);
            } else {
                startDatePicker.setValue(current);
            }

    }
}