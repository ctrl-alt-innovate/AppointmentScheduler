package com.evanwahrmund.appointmentscheduler.controllers;


import java.sql.SQLException;
import java.time.*;
import java.time.temporal.TemporalAccessor;
import java.util.function.Predicate;

import com.evanwahrmund.appointmentscheduler.Loader;
import com.evanwahrmund.appointmentscheduler.controllers.AppointmentsController;
import com.evanwahrmund.appointmentscheduler.daos.AppointmentDatabaseDao;
import com.evanwahrmund.appointmentscheduler.models.*;
import com.evanwahrmund.appointmentscheduler.util.Util;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.StringConverter;

/**
 * Controller for the FXML file: schedules.fxml.
 * Displays Appointment schedules filtered by User choice.
 * Allows User to adjust appointment times.
 * <p>
 * DISCUSSION OF LAMBDA 1: filterList(ObservableList<Appointment> apps)
 */
public class SchedulesController {
    /**
     * TableView containing all appointments
     */
    @FXML private TableView<Appointment> schedulesTable;
    /**
     * col for Appointment_ID
     */
    @FXML private TableColumn<Appointment, Integer> idCol;
    /**
     * col for Title
     */
    @FXML private TableColumn<Appointment, String> titleCol;
    /**
     * col for Type
     */
    @FXML private TableColumn<Appointment, String> typeCol;
    /**
     * col for Description
     */
    @FXML private TableColumn<Appointment, String> descriptionCol;
    /**
     * col for Location
     */
    @FXML private TableColumn<Appointment, String> locationCol;
    /**
     * col for Contact name
     */
    @FXML private TableColumn<Appointment, Contact> contactCol;
    /**
     * col for start time
     */
    @FXML private TableColumn<Appointment, ZonedDateTime> startDateTimeCol;
    /**
     * col for end time
     */
    @FXML private TableColumn<Appointment, ZonedDateTime> endDateTimeCol;
    /**
     * col for customer id
     */
    @FXML private TableColumn<Appointment, Customer> customerCol;
    /**
     * col for user id
     */
    @FXML private TableColumn<Appointment, User> userCol;
    /**
     * combobox for month/week choice
     */
    @FXML private ComboBox<TemporalAccessor> choiceComboBox;
    /**
     * label for choiceComboBox
     */
    @FXML private Label choiceLabel;
    /**
     * toggle group for radio buttons
     */
    @FXML private ToggleGroup schedulesGroup;
    /**
     * radio button for week options
     */
    @FXML private RadioButton weekRadioButton;
    /**
     * radio button for month options
     */
    @FXML private RadioButton monthRadioButton;
    /**
     * radio button for all appointments
     */
    @FXML private RadioButton allRadioButton;
    /**
     * button to fill textfields selected appointment vals
     */
    @FXML private Button modifyButton;
    /**
     * button to save changes to appointment
     */
    @FXML private Button saveButton;

    /**
     * start date for appoointment
     */
    @FXML private DatePicker startDatePicker;
    /**
     * end date for appointment
     */
    @FXML private DatePicker endDatePicker;
    /**
     * start time for appintment
     */
    @FXML private ComboBox<LocalTime> startTimeComboBox;
    /**
     * end time for appointment
     */
    @FXML private ComboBox<LocalTime> endTimeComboBox;
    /**
     * disabled textfield to display appiontment id of selected appointment for modification
     */
    @FXML private TextField idTextField;
    /**
     * menubar for main menu
     */
    @FXML private MenuBar applicationMenuBar;
    /**
     * displays current page
     */
    @FXML private Menu schedulesMenu;
    /**
     * edit appointments and customers menu
     */
    @FXML private Menu editInfoMenu;
    /**
     * edit customers
     */
    @FXML private MenuItem editCus;
    /**
     * edit appointments
     */
    @FXML private MenuItem editApps;
    /**
     * reports menu
     */
    @FXML private Menu reportsMenu;
    /**
     * apps by type report
     */
    @FXML private MenuItem appsByType;
    /**
     * apps and cus by location report
     */
    @FXML private MenuItem appsAndCusByLoc;
    /**
     * contact schedules report
     */
    @FXML private MenuItem contactSchedules;
    /**
     * logut menu
     */
    @FXML private Menu logoutMenu;
    /**
     * option to logout of application
     */
    @FXML private MenuItem logout;

    /**
     * current time
     */
    private LocalDate current = LocalDateTime.now().toLocalDate();
    /**
     * flag to detect start day change
     */
    private Boolean startChanged = false;
    /**
     * flag to detect end day change
     */
    private Boolean endChanged = false;

    /**
     * Initializes tables, menus, radio buttons, sets handlers for buttons, and alerts User of upcoming appointments.
     * Sets date values to current date and adds listener on time comboboxes to change date if time rolls over to next day
     */
    @FXML
    public void initialize() {
        initializeTable();
        initializeMenu();
        initializeRadioButtons();

        //setMonthOptions();
        //choiceComboBox.setItems(options);
        schedulesTable.setItems(Appointments.getAppointments());
        modifyButton.setOnAction(event -> modifyAppTime());
        saveButton.setOnAction(event -> updateAppTime());

        choiceComboBox.setOnAction(event -> {
            if (choiceComboBox.getValue() != null)

                schedulesTable.setItems(filterList(setSchedule()));

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

        startDatePicker.setValue(current);
        endDatePicker.setValue(current);
        initializeComboBoxes();

        Platform.runLater(() -> {
            notifyUpcomingAppointment();
        });

        startDatePicker.setOnAction(event -> {
            startChanged = false;
            if(!startTimeComboBox.getItems().contains(LocalTime.of(0,0))){
                endDatePicker.setValue(startDatePicker.getValue());
            }
        });
        startTimeComboBox.valueProperty().addListener((obs, old, newVal) -> {
            if(startTimeComboBox.getItems().contains(LocalTime.of(0,0))) {
                if (startTimeComboBox.getItems().indexOf(newVal) >= startTimeComboBox.getItems().indexOf(LocalTime.of(0, 0))
                        && startChanged == false) {
                    startDatePicker.setValue(startDatePicker.getValue().plusDays(1));
                    startChanged = true;
                } else if (startTimeComboBox.getItems().indexOf(newVal) >= startTimeComboBox.getItems().indexOf(LocalTime.of(0, 0))
                        && startChanged == true) {
                    System.out.println("NO change");
                } else if (startTimeComboBox.getItems().indexOf(newVal) < startTimeComboBox.getItems().indexOf(LocalTime.of(0, 0))
                        && startChanged == false)
                    System.out.println("NO change");
                else {
                    startDatePicker.setValue(startDatePicker.getValue().minusDays(1));
                }
            }
        });

        endDatePicker.setOnAction(event -> {
            endChanged = false;
            if(!endTimeComboBox.getItems().contains(LocalTime.of(0,0))){
                startDatePicker.setValue(endDatePicker.getValue());
            }
        });
        endTimeComboBox.valueProperty().addListener((obs, old, newVal) -> {
            if(endTimeComboBox.getItems().contains(LocalTime.of(0,0))) {
                if (endTimeComboBox.getItems().indexOf(newVal) >= endTimeComboBox.getItems().indexOf(LocalTime.of(0, 0))
                        && endChanged == false) {
                    endDatePicker.setValue(endDatePicker.getValue().plusDays(1));
                    endChanged = true;
                } else if (endTimeComboBox.getItems().indexOf(newVal) >= endTimeComboBox.getItems().indexOf(LocalTime.of(0, 0))
                        && endChanged == true) {
                    System.out.println("NO change");
                } else if (endTimeComboBox.getItems().indexOf(newVal) < endTimeComboBox.getItems().indexOf(LocalTime.of(0, 0))
                        && endChanged == false)
                    System.out.println("NO change");
                else {
                    endDatePicker.setValue(endDatePicker.getValue().minusDays(1));
                }
            }
        });



        //populateTimeComboBoxes();
    }

    /**
     * Initializes columns for schedulesTable
     */
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
        userCol.setCellValueFactory(cell -> new ReadOnlyObjectWrapper(cell.getValue().getUser().getUserId()));

    }

    /**
     * Initializes start and end time combo boxes with local times that are equivalent to hours of operation in EST
     */
    private void initializeComboBoxes(){
        ObservableList<LocalTime> options = FXCollections.observableArrayList();
        LocalDate local = startDatePicker.getValue();

        ZonedDateTime zdt = ZonedDateTime.of(LocalDateTime.of(local, LocalTime.of(8,0)), ZoneId.of("America/New_York") );

        zdt = zdt.withZoneSameInstant(ZoneId.systemDefault());

        int count = 0;
        while (count <= 24){
            LocalTime option = zdt.plusMinutes(count * 30).toLocalTime();
            //System.out.println(option.toLocalDate());
            options.add(option);
            count++;
        }
        startTimeComboBox.setItems(options);
        endTimeComboBox.setItems(options);
        //startTimeComboBox.setValue(null);
    }

    /**
     * Initializes menu items to load corresponding FXML pages and sets logout action
     */
    private void initializeMenu(){
        appsByType.setOnAction(event -> Loader.Load("FXML/apps_by_type_and_month_report.fxml", "APPS BY TYPE AND MONTH REPORT"));
        editCus.setOnAction(event -> Loader.Load("FXML/customers.fxml", "CUSTOMERS"));
        editApps.setOnAction(event -> Loader.Load("FXML/appointments.fxml", "APPOINTMENTS"));
        appsAndCusByLoc.setOnAction( event -> Loader.Load("FXML/apps_and_cus_by_loc_report.fxml", "APPS AND CUS BY LOCATION REPORT"));
        contactSchedules.setOnAction(event -> Loader.Load("FXML/apps_by_contact_report.fxml", "CONTACT SCHEDULES"));
        logout.setOnAction(actionEvent -> {
            Stage stage = ((Stage)saveButton.getScene().getWindow());
            stage.close();
            Loader.Load("FXML/login.fxml", "LOGIN");
        });
    }

    /**
     * Sets actions for radio buttons.
     * Month and week radio buttons change items of choiceComboBox and clears schedulesTable.
     * All radio button sets schedulesTable to all Appointments
     */
    private void initializeRadioButtons(){

        weekRadioButton.setOnAction(event -> {
            choiceLabel.setText("Week");
            //setWeekOptions();
            choiceComboBox.setItems(setWeekOptions());
            schedulesTable.setItems(null);
        });
        monthRadioButton.setOnAction(event -> {
            choiceLabel.setText("Month");
            //setMonthOptions();
            choiceComboBox.setItems(setMonthOptions());
            schedulesTable.setItems(null);
        });
        allRadioButton.setOnAction(event -> {
            choiceLabel.setText("Viewing All Appointments");
            schedulesTable.setItems(Appointments.getAppointments());
            choiceComboBox.setItems(null);

        });
        allRadioButton.setSelected(true);
    }

    /**
     * Creates alert, checks for upcoming appointments, and displays custom message in Alert according to results.
     * Displays Appointment info if upcoming, otherwise displays notifies of no upcoming Appointments
     */
    private void notifyUpcomingAppointment() {
        Alert notification = new Alert(Alert.AlertType.INFORMATION);
        notification.setHeaderText("Upcoming Appointments");
        StringBuilder sb = new StringBuilder();
        for(Appointment a: Appointments.getAppointments()) {
            long min = checkIfUpcoming(a);
            if(min > 0){
                System.out.println("Up");
                sb.append("Upcoming Apppointment - ID: " + a.getId() + "-" + a.getTitle() + " at: "
                        + Util.formatDateTime(a.getStartDateTime().withZoneSameInstant(ZoneId.systemDefault()))
            + " starting in " + min + " minutes." + "\n");
            }
        }
        if(sb.length() == 0){
            notification.setContentText("No Upcoming Appointments.");
        }else{
            notification.setContentText(sb.toString());
        }
        notification.show();
    }

    /**
     * Checks if Appointment is upcoming in the next 15 minutes
     * @param app Appointment to check if within 15 minutes from current time
     * @return long number of minutes between current time and appointment start if appointment is upcoming,
     * 0 if appoingment is not upcomining
     */
    private long checkIfUpcoming(Appointment app){
        LocalDateTime nowLocal = LocalDateTime.now();
        ZonedDateTime now = ZonedDateTime.of(nowLocal, ZoneId.systemDefault());

        ZonedDateTime appStart = app.getStartDateTime();
        appStart = appStart.withZoneSameInstant(ZoneId.systemDefault());

        if(now.isAfter(appStart.minusMinutes(15))&& now.isBefore(appStart)) {
            Duration untilStart = Duration.between(now, appStart);
            long min = untilStart.toMinutes() + 1;
            return min;
        }
        else
            return 0;




    }

    /**
     * Filters list of all Appointments to contain all Appointments in argument list
     * <p>
     * DISCUSSION OF LAMBDA 1: filter is a predicate used to filter the list of all Appointments to contain all appointments
     * in the argument list.  This enables the items in the schedulesTable to stay unchanged. This ensures any changes made
     * to the appointment times while the list is filtered are reflected when the list is unfiltered again.
     * @param apps ObservableList of Appointments that need to be in FilteredList
     * @return FilteredList of all Appointments containing all items in argument list
     */
    private FilteredList<Appointment> filterList(ObservableList<Appointment> apps){
        Predicate<Appointment> filter = app -> apps.contains(app);
        FilteredList<Appointment> filtered = new FilteredList<>(Appointments.getAppointments(), filter);
        return filtered;
    }

    /**
     * Gets list of all appointments that correspond to User choice in the choiceComboBox
     * @return ObservableList containing appointments of week/month choice
     */
    private ObservableList<Appointment> setSchedule() {
        ObservableList currentSchedule = FXCollections.observableArrayList();
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

    /**
     * Sets User Interface fields to values of selected Appointment
     */
    private void modifyAppTime() {
        Appointment appointment = schedulesTable.getSelectionModel().getSelectedItem();
        idTextField.setText(String.valueOf(appointment.getId()));
        startDatePicker.setValue(appointment.getStartDateTime().toLocalDate());
        endDatePicker.setValue(appointment.getEndDateTime().toLocalDate());
        startTimeComboBox.setValue(appointment.getStartDateTime().withZoneSameInstant(ZoneId.systemDefault()).toLocalTime());
        endTimeComboBox.setValue(appointment.getEndDateTime().withZoneSameInstant(ZoneId.systemDefault()).toLocalTime());
    }

    /**
     * Updates selected Appointment with values in user interface fields.
     * Alerts user if update is successful or not
     */
    private void updateAppTime() {

        try{
            Appointment app = schedulesTable.getSelectionModel().getSelectedItem();
            if(app == null){
                Alert error = new Alert(Alert.AlertType.ERROR, "Please select an appointment.");
                error.setHeaderText("Update Appointment Time Error");
                error.show();
                return;
            }
            LocalDate startDate = startDatePicker.getValue();
            LocalDate endDate = endDatePicker.getValue();
            LocalTime startTime = startTimeComboBox.getValue();
            LocalTime endTime = endTimeComboBox.getValue();
            if(startDate == null || endDate == null || startTime == null || endTime == null){
                throw new IllegalArgumentException("Dates and times must not be left blank");
            }
            ZonedDateTime start = ZonedDateTime.of(LocalDateTime.of(startDate, startTime), ZoneId.systemDefault());
            start = start.withZoneSameInstant(ZoneId.of("UTC"));

            ZonedDateTime end = ZonedDateTime.of(LocalDateTime.of(endDate, endTime), ZoneId.systemDefault());
            end = end.withZoneSameInstant(ZoneId.of("UTC"));

            AppointmentsController.checkStartAndEnd(start, end);
            for(Appointment a: Appointments.getAppointments()){
                if(!(a.getId() == app.getId()))
                    AppointmentsController.checkForOverlap(a, start, end);
            }

            app.setStartDateTime(start);
            app.setEndDateTime(end);
            Appointments.updateAppointment(app);

            Alert confirm = new Alert(Alert.AlertType.INFORMATION, "Appointment ID: " + app.getId() + " - " + app.getType()
                                        + " updated successfully.");
            confirm.setHeaderText("Appointment Time Updated");
            confirm.show();

        } catch (SQLException | IllegalArgumentException ex){
            Alert error = new Alert(Alert.AlertType.ERROR, "Error updating appointment time" + "\n" + ex.getMessage());
            error.setHeaderText("Update Appointment Time Error");
            error.show();
        }
    }

    /**
     * Gets list of all YearMonths containing at least 1 Appointment
     * @return ObservableList of YearMonths
     */
    private ObservableList<TemporalAccessor> setMonthOptions() {
        ObservableList<TemporalAccessor> options = FXCollections.observableArrayList();
        for (Appointment app : Appointments.getAppointments()) {
            Month month = app.getStartDateTime().getMonth();
            int year = app.getStartDateTime().getYear();
            YearMonth yearMonth = YearMonth.of(year, month);
            if (!options.contains(yearMonth)) {
                options.add(yearMonth);
            }

        }
        return options;
    }

    /**
     * Gets list of all Weeks containing at least 1 Appointment
     * @return ObservableList of Weeks
     */
    private ObservableList<TemporalAccessor> setWeekOptions() {
        ObservableList<TemporalAccessor> options = FXCollections.observableArrayList();
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
        return options;
    }

}