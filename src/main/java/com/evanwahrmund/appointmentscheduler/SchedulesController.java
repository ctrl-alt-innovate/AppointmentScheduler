package com.evanwahrmund.appointmentscheduler;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.MonthDay;
import java.time.YearMonth;
import java.time.temporal.TemporalAccessor;
import java.util.HashMap;
import java.util.Map;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;

public class SchedulesController {

    @FXML private TableView<Appointment> schedulesTable;
    @FXML private TableColumn<Appointment, Integer> idCol;
    @FXML private TableColumn<Appointment, String> titleCol;
    @FXML private TableColumn<Appointment, String> typeCol;
    @FXML private TableColumn<Appointment, String> descriptionCol;
    @FXML private TableColumn<Appointment, String> locationCol;
    @FXML private TableColumn<Appointment, Contact> contactCol;
    @FXML private TableColumn<Appointment, LocalDateTime> startDateTimeCol;
    @FXML private TableColumn<Appointment, LocalDateTime> endDateTimeCol;
    @FXML private TableColumn<Appointment, Customer> customerCol;

    @FXML private ComboBox<TemporalAccessor> choiceComboBox;
    @FXML private Label choiceLabel;
    @FXML private ToggleGroup schedulesGroup;
    @FXML private RadioButton weekRadioButton;
    @FXML private RadioButton monthRadioButton;

    @FXML private Button modifyButton;
    @FXML private Button saveButton;

    @FXML private TextField startTimeTextField;
    @FXML private TextField endTimeTextField;
    @FXML private TextField startDateTextField;
    @FXML private TextField endDateTextField;

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

    private ObservableList<TemporalAccessor> weekOptions = FXCollections.observableArrayList();
    private ObservableList<YearMonth> monthOptions;

    private Map<LocalDateDuration, ObservableList<Appointment>> dateToApps = new HashMap<>();

    public void initialize(){
        monthRadioButton.setSelected(true);
        initializeTable();
        //initializeWeekComboBox();
        initializeMonthComboBox();
        choiceComboBox.setItems(weekOptions);
        schedulesTable.setItems(null);
        modifyButton.setOnAction(event -> modifyAppTime());
        saveButton.setOnAction(event -> updateAppTime());

        appsByType.setOnAction(event -> Loader.Load("FXML/apps_by_type_and_month_report.fxml", "APPS BY TYPE AND MONTH REPORT"));
        editCus.setOnAction(event -> Loader.Load("FXML/customers.fxml", "CUSTOMERS"));
        editApps.setOnAction(event -> Loader.Load("FXML/appointments.fxml", "APPOINTMENTS"));
        //appsAndCusByLoc.setOnAction( event -> Loader.Load("FXML/apps_and_cus_by_loc_report.fxml", "APPS AND CUS BY LOCATION REPORT"));
        contactSchedules.setOnAction( event -> Loader.Load("FXML/apps_by_contact_report.fxml", "CONTACT SCHEDULES"));

        weekRadioButton.setOnAction(event -> {
            choiceLabel.setText("Week");
            choiceComboBox.setItems(weekOptions);
            schedulesTable.setItems(null);
        });/*
        monthRadioButton.setOnAction(event -> {
            choiceLabel.setText("Month");
            choiceComboBox.setItems(monthOptions);
            schedulesTable.setItems(null);
        });*/
        choiceComboBox.setOnAction(event -> {
            if(choiceComboBox.getValue() != null)
                schedulesTable.setItems(dateToApps.get(choiceComboBox.getValue()));
        });
        /*choiceComboBox.setConverter(new StringConverter<LocalDateDuration>() {
            @Override
            public String toString(LocalDateDuration localDateDuration) {
                if(localDateDuration != null){
                    return weekRadioButton.isSelected() ?
                            "Week of: " + localDateDuration.getDate().getMonthValue() + "/" + localDateDuration.getDate().getDayOfMonth() + "/" + localDateDuration.getDate().getYear() :
                            "Month of: " + localDateDuration.getDate().getMonth().toString().substring(0,1) + localDateDuration.getDate().getMonth().toString().toLowerCase().substring(1) +
                                    "/" + localDateDuration.getDate().getYear();

                }
                return null;
            }
            @Override
            public LocalDateDuration fromString(String s) {
                return null;
            }
        });*/
    }

    private void modifyAppTime() {
        Appointment appointment = schedulesTable.getSelectionModel().getSelectedItem();
        startDateTextField.setText(Util.formatDate(appointment.getStartDateTime().toLocalDate()));
        startTimeTextField.setText(Util.formatTime(appointment.getStartDateTime().toLocalTime()));
        endDateTextField.setText(Util.formatDate(appointment.getEndDateTime().toLocalDate()));
        endTimeTextField.setText(Util.formatTime(appointment.getEndDateTime().toLocalTime()));
    }
    private void updateAppTime() {
        Appointment appointment = schedulesTable.getSelectionModel().getSelectedItem();
        LocalDateTime start = LocalDateTime.of(Util.stringToDate(startDateTextField.getText()),
                                            Util.stringToTime(startTimeTextField.getText()));
        LocalDateTime end = LocalDateTime.of(Util.stringToDate(endDateTextField.getText()),
                Util.stringToTime(endTimeTextField.getText()));
        appointment.setStartDateTime(start);
        appointment.setEndDateTime(end);
        AppointmentDatabaseDao.getInstance().updateAppTime(appointment);
        schedulesTable.refresh();
    }
    /*private void initializeMonthComboBox(){
        monthOptions = FXCollections.observableArrayList();
        Duration duration = Duration.ofDays(0);
        for(Appointment appointment: Appointments.getAppointments()){
            LocalDate month = appointment.getStartDateTime().toLocalDate();
            while(month.getDayOfMonth()  > 1) {
                month = month.minusDays(1);
            }
            int monthVal = month.getMonthValue();
            LocalDate date = month;
            while(month.getMonthValue() == monthVal){
                duration = duration.plusDays(1);
                month = month.plusDays(1);
            }
            Boolean alreadyListed = false;
            for(LocalDateDuration dateDuration: monthOptions){
                if (dateDuration.getDate().getMonthValue() == monthVal){
                    alreadyListed = true;
                }
            }
            if(!alreadyListed){
                LocalDateDuration localDateDuration= new LocalDateDuration(date,duration);
                monthOptions.add(localDateDuration);
            }
        }
        mapAppsToMonth(monthOptions);
    }*/
    private void initializeMonthComboBox(){
        weekOptions.clear();
        for(Appointment app : Appointments.getAppointments()){
            Month month = app.getStartDateTime().getMonth();
            int year = app.getStartDateTime().getYear();
            YearMonth yearMonth = YearMonth.of(year, month);
            if (weekOptions.contains(yearMonth)){
                weekOptions.add(yearMonth);
            }

        }
    }/*
    private void initializeWeekComboBox(){
        weekOptions = FXCollections.observableArrayList();
        for(Appointment appointment: Appointments.getAppointments()){
            LocalDate week = appointment.getStartDateTime().toLocalDate();
            while(week.getDayOfWeek() != DayOfWeek.SUNDAY){
                week = week.minusDays(1);
            }
            LocalDateDuration dateDuration = new LocalDateDuration(week, Duration.ofDays(7));
            if (!weekOptions.contains(dateDuration))
                weekOptions.add(dateDuration);
        }
        mapAppsToWeek(weekOptions);
    }*/

    private void initializeTable(){
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        locationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        contactCol.setCellValueFactory(cell -> new ReadOnlyObjectWrapper(cell.getValue().getContact().getName()));
        startDateTimeCol.setCellValueFactory(cell -> new ReadOnlyObjectWrapper(Util.formatDateTime(cell.getValue().getStartDateTime())));
        endDateTimeCol.setCellValueFactory(cell -> new ReadOnlyObjectWrapper(Util.formatDateTime(cell.getValue().getEndDateTime())));
        customerCol.setCellValueFactory(cell -> new ReadOnlyObjectWrapper(cell.getValue().getCustomer().getId()));

    }
    private void mapAppsToWeek(ObservableList<LocalDateDuration> weeks) {
        for (LocalDateDuration date : weeks) {
            dateToApps.put(date, AppointmentDatabaseDao.getInstance().getFilteredAppointments(date.getDate(), date.getDate().plusDays(6)));
        }
    }

    private void mapAppsToMonth(ObservableList<LocalDateDuration> months) {
        for (LocalDateDuration date : months) {
            LocalDate start = date.getDate();
            LocalDate end = start;
            int monthVal = end.getMonthValue();
            while(end.getMonthValue() == monthVal){
                end = end.plusDays(1);
                //System.out.println(end.getMonthValue() + "/" + end.getDayOfMonth());
            }
            end.minusDays(1);
            dateToApps.put(date, AppointmentDatabaseDao.getInstance().getFilteredAppointments(start, end));
        }
    }

    public class LocalDateDuration{
        private LocalDate date;
        private Duration duration;
        public LocalDateDuration (LocalDate date, Duration duration){
            this.date = date;
            this.duration = duration;
        }
        public LocalDate getDate(){return date;}
        @Override public String toString(){
            return duration.toDays() > 7 ?
                    date.getMonth() + ":" + date.getYear() :
                    date.getMonthValue() + "/" + date.getDayOfMonth() + "/"+ date.getYear();


        }
    }
}