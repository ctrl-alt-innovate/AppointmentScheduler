package com.evanwahrmund.appointmentscheduler;

import java.time.*;
import java.time.temporal.TemporalAccessor;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;

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
    private Button modifyButton;
    @FXML
    private Button saveButton;

    @FXML
    private DatePicker startDatePicker;
    @FXML
    private DatePicker endDatePicker;
    @FXML
    private ComboBox<LocalTime> startTimeComboBox;
    @FXML
    private ComboBox<LocalTime> endTimeComboBox;

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
    private Menu logout;

    private ObservableList<TemporalAccessor> options = FXCollections.observableArrayList();
    private ObservableList<YearMonth> monthOptions;


    public void initialize() {
        monthRadioButton.setSelected(true);
        initializeTable();
        //initializeWeekComboBox();
        setMonthOptions();
        choiceComboBox.setItems(options);
        schedulesTable.setItems(null);
        modifyButton.setOnAction(event -> modifyAppTime());
        //saveButton.setOnAction(event -> updateAppTime());

        appsByType.setOnAction(event -> Loader.Load("FXML/apps_by_type_and_month_report.fxml", "APPS BY TYPE AND MONTH REPORT"));
        editCus.setOnAction(event -> Loader.Load("FXML/customers.fxml", "CUSTOMERS"));
        editApps.setOnAction(event -> Loader.Load("FXML/appointments.fxml", "APPOINTMENTS"));
        //appsAndCusByLoc.setOnAction( event -> Loader.Load("FXML/apps_and_cus_by_loc_report.fxml", "APPS AND CUS BY LOCATION REPORT"));
        contactSchedules.setOnAction(event -> Loader.Load("FXML/apps_by_contact_report.fxml", "CONTACT SCHEDULES"));

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
        populateTimeComboBoxes();
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
        startTimeComboBox.setValue(appointment.getStartDateTime().toLocalTime());

        endTimeComboBox.setValue(appointment.getEndDateTime().toLocalTime());


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
                .withZoneSameInstant(ZoneId.of("UTC-05:00"))))); // add system defaults
        endDateTimeCol.setCellValueFactory(cell -> new ReadOnlyObjectWrapper(Util.formatDateTime(cell.getValue().getEndDateTime()
                .withZoneSameInstant(ZoneId.of("UTC-05:00"))))); //add system defaults
        customerCol.setCellValueFactory(cell -> new ReadOnlyObjectWrapper(cell.getValue().getCustomer().getId()));

    }
    private void populateTimeComboBoxes(){
        ObservableList<LocalTime> options = FXCollections.observableArrayList();
        LocalTime beggining = LocalTime.of(8, 0);
        int count = 0;
        while (count <= 24){
            LocalTime option = beggining.plusMinutes(count * 30);
            options.add(option);
            count++;
        }
        startTimeComboBox.setItems(options);
        endTimeComboBox.setItems(options);
    }

}