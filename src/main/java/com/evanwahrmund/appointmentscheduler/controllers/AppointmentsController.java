package com.evanwahrmund.appointmentscheduler.controllers;

import java.sql.SQLException;
import java.time.*;

import com.evanwahrmund.appointmentscheduler.models.*;
import com.evanwahrmund.appointmentscheduler.util.Util;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * Controller for FXML file: appointments.fxml.
 * Displays all Appointments and allows User to add, edit, and delete Appointments.
 */
public class AppointmentsController {
    /**
     * Contains all Appointments
     */
    @FXML private TableView<Appointment> appointmentsTable;
    /**
     * Column for Appointment_ID
     */
    @FXML private TableColumn<Appointment, Integer> idCol;
    /**
     * Column for Title
     */
    @FXML private TableColumn<Appointment, String> titleCol;
    /**
     * Column for Type
     */
    @FXML private TableColumn<Appointment, String> typeCol;
    /**
     * column for Description
     */
    @FXML private TableColumn<Appointment, String> descriptionCol;
    /**
     * column for Location
     */
    @FXML private TableColumn<Appointment, String> locationCol;
    /**
     * column for Contact name
     */
    @FXML private TableColumn<Appointment, Contact> contactCol;
    /**
     * column for start time
     */
    @FXML private TableColumn<Appointment, ZonedDateTime> startDateTimeCol;
    /**
     * column for end time
     */
    @FXML private TableColumn<Appointment, ZonedDateTime> endDateTimeCol;
    /**
     * column for Customer id
     */
    @FXML private TableColumn<Appointment, Customer> customerCol;
    /**
     * column for User id
     */
    @FXML private TableColumn<Appointment, User> userCol;
    /**
     * disabled textfield that displays id when updating Appointments
     */
    @FXML private TextField idTextField;
    /**
     * textfield to collect description
     */
    @FXML private TextField descriptionTextField;
    /**
     * textfield to collect title
     */
    @FXML private TextField titleTextField;
    /**
     * textfield to collect type
     */
    @FXML private TextField typeTextField;
    /**
     * textfield to collect location
     */
    @FXML private TextField locationTextField;
    /**
     *combobox to display local start times that are equivalent to business hours EST
     */
    @FXML private ComboBox<LocalTime> startTimeComboBox;
    /**
     * combobox to display local end times that equivalent to business hours EST
     */
    @FXML private ComboBox<LocalTime> endTimeComboBox;
    /**
     * DatePicker to select appointment start date
     */
    @FXML private DatePicker startDatePicker;
    /**
     * DatePicker to slect appointment end date
     */
    @FXML private DatePicker endDatePicker;
    /**
     * textfield to collect user id
     */
    @FXML private TextField userIdTextField;
    /**
     * textfield to collect customer id
     */
    @FXML private TextField customerIdTextField;
    /**
     * ComboBox to assign Contact
     */
    @FXML private ComboBox<Contact> contactComboBox;


    /**
     * button to add new Appointment
     */
    @FXML private Button addButton;
    /**
     * button to fill text fields with vals of selected appointment
     */
    @FXML private Button modifyButton;
    /**
     * button to deleted selected appointment
     */
    @FXML private Button deleteButton;
    /**
     * button to save changes to existing appointment
     */
    @FXML private Button saveButton;
    /**
     * button to clear fields and set date to current
     */
    @FXML private Button resetButton;
    /**
     * used as a flag to detect if start date has changed
     */
    private Boolean startChanged = false;
    /**
     * used as a flag to detect if end date changed
     */
    private Boolean endChanged = false;

    /**
     * Populates contacts in contactComboBox, appointments in appointments table, and LocalTimes in time combo boxes.
     * Assigns button handlers to respective methods. Links start and end date pickers if time options do not carry over to next day.
     * If time options carry over to next day, date pickers adjust accordingly
     */
    public void initialize(){
        initializeTable();
        contactComboBox.setItems(Contacts.getContacts());

        appointmentsTable.setItems(Appointments.getAppointments());
        resetButton.setOnAction(event -> resetFields());
        addButton.setOnAction(event -> createAppointment());
        modifyButton.setOnAction(event -> modifyAppointment());
        saveButton.setOnAction(event -> updateAppointment());
        deleteButton.setOnAction(event -> deleteAppointment());



        startDatePicker.setValue(LocalDate.now());
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
                    //startDatePicker.setValue(startDatePicker.getValue());
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


        endDatePicker.setValue(LocalDate.now());
        endDatePicker.setOnAction(event -> {
            endChanged = false;
            if(!endTimeComboBox.getItems().contains(LocalTime.of(0,0))){
                startDatePicker.setValue(endDatePicker.getValue());
            }

        });
        endTimeComboBox.valueProperty().addListener((obs, old, newVal) -> {
            if(endTimeComboBox.getItems().contains(LocalTime.of(0,0))){
                if(endTimeComboBox.getItems().indexOf(newVal) >= endTimeComboBox.getItems().indexOf(LocalTime.of(0,0))
                        && endChanged == false) {
                    endDatePicker.setValue(endDatePicker.getValue().plusDays(1));
                    endChanged = true;
                }else if(endTimeComboBox.getItems().indexOf(newVal) >= endTimeComboBox.getItems().indexOf(LocalTime.of(0,0))
                        && endChanged == true){
                    System.out.println("NO change");
                }else if(endTimeComboBox.getItems().indexOf(newVal) < endTimeComboBox.getItems().indexOf(LocalTime.of(0,0))
                        && endChanged == false)
                    System.out.println("NO change");
                else {
                    endDatePicker.setValue(endDatePicker.getValue().minusDays(1));
                }

            }
        });
        populateTimeComboBoxes();
        //loadTestData();
    }

    /**
     * Initializes appointmentsTable
     */
    private void initializeTable(){
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        locationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        contactCol.setCellValueFactory(cell -> new ReadOnlyObjectWrapper(cell.getValue().getContact().getName()));
        startDateTimeCol.setCellValueFactory(cell -> new ReadOnlyObjectWrapper(Util.formatDateTime(cell.getValue().getStartDateTime()
                .withZoneSameInstant((ZoneId.systemDefault())))));
        endDateTimeCol.setCellValueFactory(cell -> new ReadOnlyObjectWrapper(Util.formatDateTime(cell.getValue().getEndDateTime()
                .withZoneSameInstant(ZoneId.systemDefault()))));
        customerCol.setCellValueFactory(cell -> new ReadOnlyObjectWrapper(cell.getValue().getCustomer().getId()));
        userCol.setCellValueFactory(cell -> new ReadOnlyObjectWrapper(cell.getValue().getUser().getUserId()));

    }

    /**
     * Initializes start and end time combo boxes with local times that are equivalent to hours of operation in EST
     */
    private void populateTimeComboBoxes() {
        ObservableList<LocalTime> options = FXCollections.observableArrayList();
        LocalTime beginning = LocalTime.of(8, 0);
        LocalDate date = startDatePicker.getValue();
        ZonedDateTime zdt = ZonedDateTime.of(LocalDateTime.of(date, beginning), ZoneId.of("UTC-05:00"));
        zdt = zdt.withZoneSameInstant(ZoneId.systemDefault());
        int count = 0;
        while (count <= 24){
            LocalTime time = zdt.toLocalTime().plusMinutes(count * 30);
            options.add(time);
            count++;
        }
        startTimeComboBox.setItems(options);
        endTimeComboBox.setItems(options);
    }

    /**
     * clears textfields and comboBoxes. Sets start and end dates to current.
     */
    public void resetFields(){
        idTextField.clear();
        titleTextField.clear();
        descriptionTextField.clear();
        locationTextField.clear();
        typeTextField.clear();
        startTimeComboBox.setValue(null);
        endTimeComboBox.setValue(null);
        startDatePicker.setValue(LocalDate.now());
        endDatePicker.setValue(LocalDate.now());
        customerIdTextField.clear();
        userIdTextField.clear();
        contactComboBox.setValue(null);
    }

    /**
     * Creates new Appointment and displays confirmation alert if successful. Displays Error alert if any fields or comboboxes
     * are blank. Checks that user, contact, and customer choices are valid. Checks for appointment overlap and displays error
     * message if there are any overlaps.
     */
    public void createAppointment(){
        try {
            String title = titleTextField.getText();
            String description = descriptionTextField.getText();
            String location = locationTextField.getText();
            String type = typeTextField.getText();


            LocalDate startDate = startDatePicker.getValue();
            LocalDate endDate = endDatePicker.getValue();
            LocalTime startTime = startTimeComboBox.getValue();
            LocalTime endTime = endTimeComboBox.getValue();

            if (title.isBlank() || description.isBlank() || location.isBlank() || type.isBlank() || startDate == null ||
                    endDate == null || startTime == null || endTime == null) {
                throw new IllegalArgumentException("Fields Can not be left blank");
            }

            Customer customer = Customers.getCustomer(Integer.parseInt(customerIdTextField.getText()));
            User user = Users.getUser(Integer.parseInt(userIdTextField.getText()));
            Contact contact = contactComboBox.getValue();

            if (customer == null || user == null || contact == null) {
                throw new IllegalArgumentException("Please select a valid contact, customer, and user.");
            }

            ZonedDateTime start = ZonedDateTime.of(LocalDateTime.of(startDate, startTime), ZoneId.systemDefault());
            start = start.withZoneSameInstant(ZoneId.of("UTC"));

            ZonedDateTime end = ZonedDateTime.of(LocalDateTime.of(endDate, endTime), ZoneId.systemDefault());
            end = end.withZoneSameInstant(ZoneId.of("UTC"));

            checkStartAndEnd(start, end);

            for (Appointment app : Appointments.getAppointments()) {
                checkForOverlap(app, start, end);
            }
            Appointment appointment = new Appointment(title, description, location, type, start, end, customer, user, contact);
            Appointments.createAppointment(appointment);
            Alert confirm = new Alert(Alert.AlertType.INFORMATION, "Appointment ID: " + appointment.getId() + " - " +
                    appointment.getType() + " created successfully.");
            confirm.setHeaderText("Appointment Added");
            confirm.show();
            resetFields();

        } catch (NullPointerException | IllegalArgumentException | SQLException ex){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error creating Appointment. Please ensure all fields are filled and valid.");
            alert.setContentText(alert.getContentText() + "\n" + ex.getMessage());
            alert.show();
        }

    }

    /**
     * Checks that appointment start time is not after end time.
     * @param start ZonedDateTime of appointment start
     * @param end ZonedDateTime of appointment end
     * @throws IllegalArgumentException if start time is after end time
     */
    public static void checkStartAndEnd(ZonedDateTime start, ZonedDateTime end) throws IllegalArgumentException{
        if(start.isAfter(end)){
            throw new IllegalArgumentException("Start time can not be after end time.");

        }
    }

    /**
     * Checks proposed start and end times to see if they overlap with any existing appointment times.
     * @param existing Appointment to check
     * @param start ZonedDateTime proposed start
     * @param end ZonedDateTime proposed end
     * @throws IllegalArgumentException if proposed start and end times overlap with exsisting appointment
     */
    public static void checkForOverlap(Appointment existing, ZonedDateTime start, ZonedDateTime end)  throws IllegalArgumentException {
        ZonedDateTime existingStart = existing.getStartDateTime();
        ZonedDateTime existingEnd = existing.getEndDateTime();
        //1. proposed equal to existing
        if((existingStart.isEqual(start)) && (existingEnd.isEqual(end))) {
            throw new IllegalArgumentException("Apppointment already exists for this time");
        }
        //2. proposed between existing app
        if((start.isAfter(existingStart) && (end.isBefore(existingEnd)))){
            throw new IllegalArgumentException("Can not schedule overlapping appointments");
        }
        //3. existing between proposed
        if((start.isBefore(existingStart) && (end.isAfter(existingEnd)))){
            throw new IllegalArgumentException("Can not schedule overlapping appointments");
        }
        //4 starts equal and proposed before existing end
        if((start.isEqual(existingStart)) && (end.isBefore(existingEnd))){
            throw new IllegalArgumentException("Can not schedule overlapping appointments");
        }
        //5. starts equal and proposed after existing end
        if((start.isEqual(existingStart)) && (end.isAfter(existingEnd))){
            throw new IllegalArgumentException("Can not schedule overlapping appointments");
        }
        //6. ends equal and proposed before existing start
        if((start.isBefore(existingStart)) && (end.isEqual(existingEnd))){
            throw new IllegalArgumentException("Can not schedule overlapping appointments");
        }
        //7. ends equal and proposed after existing start
        if((start.isAfter(existingStart)) && (end.isEqual(existingEnd))){
            throw new IllegalArgumentException("Can not schedule overlapping appointments");
        }
        //8 proposed start before existing and proposed end between existing start and end
        if((start.isBefore(existingStart)) && ((end.isAfter(existingStart)) && end.isBefore(existingEnd))){
            throw new IllegalArgumentException("Can not schedule overlapping appointments");
        }
        //9 proposed end after existing and proposed start between existing start and end
        if((end.isAfter(existingEnd)) && ((start.isAfter(existingStart)) && start.isBefore(existingEnd))){
            throw new IllegalArgumentException("Can not schedule overlapping appointments");
        }
    }

    /**
     * Fills textfields and comboboxes with values of selected appointment. Displays error alert if no appointment selected.
     */
    public void modifyAppointment(){
        Appointment appointment = appointmentsTable.getSelectionModel().getSelectedItem();
        if(appointment == null) {
            Alert error = new Alert(Alert.AlertType.ERROR, "No Appointment selected.");
            error.setHeaderText("Modify Appointment Error");
            error.show();
            return;
        }
        idTextField.setText(String.valueOf(appointment.getId()));
        titleTextField.setText(appointment.getTitle());
        descriptionTextField.setText(appointment.getDescription());
        locationTextField.setText(appointment.getLocation());
        typeTextField.setText(appointment.getType());
        startTimeComboBox.setValue(appointment.getStartDateTime().withZoneSameInstant(ZoneId.systemDefault()).toLocalTime());
        endTimeComboBox.setValue(appointment.getEndDateTime().withZoneSameInstant(ZoneId.systemDefault()).toLocalTime());
        startDatePicker.setValue(appointment.getStartDateTime().toLocalDate());
        endDatePicker.setValue(appointment.getEndDateTime().toLocalDate());
        userIdTextField.setText(String.valueOf(appointment.getUser().getUserId()));
        customerIdTextField.setText(String.valueOf(appointment.getCustomer().getId()));
        contactComboBox.setValue(appointment.getContact());
    }

    /**
     * Updates selected appointment and display confirmation alert if successful.  Display error alert if no appointment selected.
     * Displays Error alert and cancel update if any fields are blank or any time overlaps occur.
     */
    public void updateAppointment() {
        Appointment appointment = appointmentsTable.getSelectionModel().getSelectedItem();
        if(appointment == null){
            Alert error = new Alert(Alert.AlertType.ERROR, "Please select an appointment to update.");
            error.setHeaderText("Appointment Update Error");
            error.show();
            return;
        }
        try {
            if(titleTextField.getText().isBlank() || descriptionTextField.getText().isBlank() || locationTextField.getText().isBlank() ||
                typeTextField.getText().isBlank() || startDatePicker.getValue() == null || endDatePicker.getValue() == null ||
                startTimeComboBox.getValue() == null || endTimeComboBox.getValue() == null || userIdTextField.getText().isBlank() ||
                customerIdTextField.getText().isBlank() || contactComboBox.getValue() == null){
                    throw new IllegalArgumentException("Fields can not be left blank.");
            }
            Contact contact = contactComboBox.getValue();
            User user = Users.getUser(Integer.parseInt(userIdTextField.getText()));
            Customer cus = Customers.getCustomer(Integer.parseInt(customerIdTextField.getText()));
            if(contact == null || user == null || cus == null){
                throw new IllegalArgumentException("Please select a valid contact, user, and customer.");
            }

            LocalDateTime start = LocalDateTime.of(startDatePicker.getValue(), startTimeComboBox.getValue());
            ZonedDateTime startZoned = ZonedDateTime.of(start, ZoneId.systemDefault());
            startZoned = startZoned.withZoneSameInstant(ZoneId.of("UTC"));

            LocalDateTime end = LocalDateTime.of(endDatePicker.getValue(), endTimeComboBox.getValue());
            ZonedDateTime endZoned = ZonedDateTime.of(end, ZoneId.systemDefault());
            endZoned = endZoned.withZoneSameInstant(ZoneId.of("UTC"));

            checkStartAndEnd(startZoned, endZoned);
            for(Appointment a: Appointments.getAppointments()){
                if(!(a.getId() == appointment.getId())){
                    checkForOverlap(a, startZoned, endZoned);
                }
            }

            appointment.setTitle(titleTextField.getText());
            appointment.setDescription(descriptionTextField.getText());
            appointment.setLocation(locationTextField.getText());
            appointment.setType(typeTextField.getText());
            appointment.setStartDateTime(startZoned);
            appointment.setEndDateTime(endZoned);
            appointment.setCustomer(cus);
            appointment.setUser(user);
            appointment.setContact(contact);

            Appointments.updateAppointment(appointment);

            Alert confirm = new Alert(Alert.AlertType.INFORMATION, "Appointment ID: " + appointment.getId() + " - " +
                    appointment.getType() + " updated successfully.");
            confirm.setHeaderText("Appointment Updated");
            confirm.show();
            resetFields();

        } catch(IllegalArgumentException | SQLException | NullPointerException ex){
            Alert error = new Alert(Alert.AlertType.ERROR,"Error updating Appointment ID: " + appointment.getId() + " - " +
                    appointment.getType() + "\n"  + ex.getMessage());
            error.setHeaderText("Appointment Update Error");
            error.show();

        }
    }

    /**
     * Deletes selected appointment and displays confirmation alert.  Displays error alert if no appointment selected or there
     * are any problems deleting from database.
     */
    public void deleteAppointment(){
        try {
            Appointment appointment = appointmentsTable.getSelectionModel().getSelectedItem();
            if(appointment == null){
                Alert alert = new Alert(Alert.AlertType.ERROR, "No Appointment selected to delete.");
                alert.setHeaderText("Appointment Deletion Error");
                alert.show();
            }
            Appointments.deleteAppointment(appointment);

            Alert confirm = new Alert(Alert.AlertType.INFORMATION, "Appointment ID: " + appointment.getId() + " - " +
                    appointment.getType() + " deleted successfully.");
            confirm.setHeaderText("Appointment Deleted");
            confirm.show();

        } catch (SQLException ex){
            Alert error = new Alert(Alert.AlertType.ERROR, "Error deleting Appointment" + "\n" + ex.getMessage());
            error.setHeaderText("Appointment Deletion Error");
            error.show();

        }
    }
    /*private void loadTestData(){
        titleTextField.setText("title");
        locationTextField.setText("location");
        typeTextField.setText("Board Meeting");
        descriptionTextField.setText("description");
        userIdTextField.setText("1");
        customerIdTextField.setText("1");
        startDatePicker.setValue(LocalDate.now().plusDays(7));
        endDatePicker.setValue(LocalDate.now().plusDays(7));
        contactComboBox.setValue(contactComboBox.getItems().get(0));
        startTimeComboBox.setValue(startTimeComboBox.getItems().get(6));
        endTimeComboBox.setValue(endTimeComboBox.getItems().get(8));
    }*/
}