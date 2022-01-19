package com.evanwahrmund.appointmentscheduler;


import java.sql.SQLException;
import java.time.*;
import java.time.temporal.TemporalAccessor;
import java.util.function.Predicate;

import com.evanwahrmund.appointmentscheduler.daos.AppointmentDatabaseDao;
import com.evanwahrmund.appointmentscheduler.models.Appointment;
import com.evanwahrmund.appointmentscheduler.models.Appointments;
import com.evanwahrmund.appointmentscheduler.models.Contact;
import com.evanwahrmund.appointmentscheduler.models.Customer;
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

public class SchedulesController {

    @FXML private TableView<Appointment> schedulesTable;
    @FXML private TableColumn<Appointment, Integer> idCol;
    @FXML private TableColumn<Appointment, String> titleCol;
    @FXML private TableColumn<Appointment, String> typeCol;
    @FXML private TableColumn<Appointment, String> descriptionCol;
    @FXML private TableColumn<Appointment, String> locationCol;
    @FXML private TableColumn<Appointment, Contact> contactCol;
    @FXML private TableColumn<Appointment, ZonedDateTime> startDateTimeCol;
    @FXML private TableColumn<Appointment, ZonedDateTime> endDateTimeCol;
    @FXML private TableColumn<Appointment, Customer> customerCol;
    @FXML private ComboBox<TemporalAccessor> choiceComboBox;
    @FXML private Label choiceLabel;
    @FXML private ToggleGroup schedulesGroup;
    @FXML private RadioButton weekRadioButton;
    @FXML private RadioButton monthRadioButton;
    @FXML private RadioButton allRadioButton;
    @FXML private Button modifyButton;
    @FXML private Button saveButton;
    @FXML private DatePicker startDatePicker;
    @FXML private DatePicker endDatePicker;
    @FXML private ComboBox<LocalTime> startTimeComboBox;
    @FXML private ComboBox<LocalTime> endTimeComboBox;
    @FXML private MenuBar applicationMenuBar;
    @FXML private Menu schedulesMenu;
    @FXML private Menu editInfoMenu;
    @FXML private MenuItem editCus;
    @FXML private MenuItem editApps;
    @FXML private Menu reportsMenu;
    @FXML private MenuItem appsByType;
    @FXML private MenuItem appsAndCusByLoc;
    @FXML private MenuItem contactSchedules;
    @FXML private Menu logoutMenu;
    @FXML private MenuItem logout;

    //private ObservableList<TemporalAccessor> options = FXCollections.observableArrayList();
    //private ObservableList<Appointment> currentSchedule = FXCollections
    private LocalDate current = LocalDateTime.now().toLocalDate();
    private Boolean startChanged = false;
    private Boolean endChanged = false;

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

                schedulesTable.setItems(filterList(setMonthlySchedule()));

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
            notification.setContentText("No Upcoming Apps.");
        }else{
            notification.setContentText(sb.toString());
        }
        notification.show();
    }
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

        //ZonedDateTime utc = zdt.withZoneSameInstant(ZoneId.of("UTC"));
        /*if((now.isAfter(app.getStartDateTime().toLocalDateTime())) && (now.isBefore(app.getEndDateTime().toLocalDateTime()))){
            return true;
        } else {
            return false;
        }*/




    }
    private FilteredList<Appointment> filterList(ObservableList<Appointment> apps){
        Predicate<Appointment> pred = app -> apps.contains(app);
        FilteredList<Appointment> filtered = new FilteredList<>(Appointments.getAppointments(), pred);
        return filtered;
    }
    private ObservableList<Appointment> setMonthlySchedule() {
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

    private void modifyAppTime() {
        Appointment appointment = schedulesTable.getSelectionModel().getSelectedItem();
        startDatePicker.setValue(appointment.getStartDateTime().toLocalDate());
        endDatePicker.setValue(appointment.getEndDateTime().toLocalDate());
        startTimeComboBox.setValue(appointment.getStartDateTime().withZoneSameInstant(ZoneId.systemDefault()).toLocalTime());
        endTimeComboBox.setValue(appointment.getEndDateTime().withZoneSameInstant(ZoneId.systemDefault()).toLocalTime());
    }

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
    /*private void changeDay(LocalDateTime ldt) {
        /*if (ldt == null) {
            initializeComboBoxes();
            return;
        }System.out.println(ldt);
            if (ldt.toLocalDate().isAfter(current)) {
                current = current.plusDays(1);
                startDatePicker.setValue(current);
            } else {
                startDatePicker.setValue(current);
            }

    }*/
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
}