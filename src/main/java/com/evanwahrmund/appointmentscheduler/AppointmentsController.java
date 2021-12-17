package com.evanwahrmund.appointmentscheduler;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;

public class AppointmentsController {

    @FXML private TableView<Appointment> appointmentsTable;
    @FXML private TableColumn<Appointment, Integer> idCol;
    @FXML private TableColumn<Appointment, String> titleCol;
    @FXML private TableColumn<Appointment, String> typeCol;
    @FXML private TableColumn<Appointment, String> descriptionCol;
    @FXML private TableColumn<Appointment, String> locationCol;
    @FXML private TableColumn<Appointment, Contact> contactCol;
    @FXML private TableColumn<Appointment, LocalDateTime> startDateTimeCol;
    @FXML private TableColumn<Appointment, LocalDateTime> endDateTimeCol;
    @FXML private TableColumn<Appointment, Customer> customerCol;


    @FXML private TextField idTextField;
    @FXML private TextField descriptionTextField;
    @FXML private TextField titleTextField;
    @FXML private TextField typeTextField;
    @FXML private TextField locationTextField;
    @FXML private TextField startTimeTextField;
    @FXML private TextField endTimeTextField;
    @FXML private DatePicker startDatePicker;
    @FXML private DatePicker endDatePicker;
    @FXML private TextField userIdTextField;
    @FXML private TextField customerIdTextField;
    @FXML private ComboBox<Contact> contactComboBox;

    @FXML private Button addButton;
    @FXML private Button modifyButton;
    @FXML private Button deleteButton;
    @FXML private Button saveButton;
    @FXML private Button resetButton;

    public void initialize(){
        initializeTable();
        contactComboBox.setItems(Contacts.getContacts());
        /*contactComboBox.setConverter(new StringConverter<Contact>() {
            @Override
            public String toString(Contact contact) {
                if(contact != null)
                    return contact.getId() + " - " + contact.getName();
                return null;
            }

            @Override
            public Contact fromString(String s) {
                return null;
            }
        });*/


        appointmentsTable.setItems(Appointments.getAppointments());
        resetButton.setOnAction(event -> resetFields());
        addButton.setOnAction(event -> createAppointment());
        modifyButton.setOnAction(event -> modifyAppointment());
        saveButton.setOnAction(event -> updateAppointment());
        deleteButton.setOnAction(event -> deleteAppointment());

    }

    private void initializeTable(){
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        locationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        contactCol.setCellValueFactory(cell -> new ReadOnlyObjectWrapper(cell.getValue().getContact().getName()));
        startDateTimeCol.setCellValueFactory(cell -> new ReadOnlyObjectWrapper(Util.formatDateTime(cell.getValue().getStartDateTime().at)));
        endDateTimeCol.setCellValueFactory(cell -> new ReadOnlyObjectWrapper(Util.formatDateTime(cell.getValue().getEndDateTime())));
        customerCol.setCellValueFactory(cell -> new ReadOnlyObjectWrapper(cell.getValue().getCustomer().getId()));

    }
    public void resetFields(){
        idTextField.clear();
        titleTextField.clear();
        descriptionTextField.clear();
        locationTextField.clear();
        typeTextField.clear();
        startTimeTextField.clear();
        endTimeTextField.clear();
        customerIdTextField.clear();
        userIdTextField.clear();
        contactComboBox.setValue(null);
    }
    public void createAppointment(){
        String title = titleTextField.getText();
        String description = descriptionTextField.getText();
        String location = locationTextField.getText();
        String type = typeTextField.getText();
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();
        LocalTime startTime = Util.stringToTime(startTimeTextField.getText());
        LocalTime endTime = Util.stringToTime(endTimeTextField.getText());
        LocalDateTime start = LocalDateTime.of(startDate, startTime);
        LocalDateTime end = LocalDateTime.of(endDate, endTime);
        Customer customer = Customers.getCustomer(Integer.parseInt(customerIdTextField.getText()));
        User user = Users.getUser(Integer.parseInt(userIdTextField.getText()));
        Contact contact = contactComboBox.getValue();
        if (validateAppointment(title, description, location, type, start, end, customer, user, contact)) {
            Appointment appointment = new Appointment(title, description, location, type, start, end, customer, user, contact);
            Appointments.createAppointment(appointment);
            resetFields();
        }
        else{
            //notify that appointment was not created
        }
    }
    public void modifyAppointment(){
        Appointment appointment = appointmentsTable.getSelectionModel().getSelectedItem();
        idTextField.setText(String.valueOf(appointment.getId()));
        titleTextField.setText(appointment.getTitle());
        descriptionTextField.setText(appointment.getDescription());
        locationTextField.setText(appointment.getLocation());
        typeTextField.setText(appointment.getType());
        startTimeTextField.setText(appointment.getStartDateTime().toLocalTime().toString());
        startDatePicker.setValue(appointment.getStartDateTime().toLocalDate());
        endTimeTextField.setText(appointment.getEndDateTime().toLocalTime().toString());
        endDatePicker.setValue(appointment.getEndDateTime().toLocalDate());
        userIdTextField.setText(String.valueOf(appointment.getUser().getUserId()));
        customerIdTextField.setText(String.valueOf(appointment.getCustomer().getId()));
        contactComboBox.setValue(appointment.getContact());
    }
    public void updateAppointment() {
        try {
            Appointment appointment = appointmentsTable.getSelectionModel().getSelectedItem();
            appointment.setTitle(titleTextField.getText());
            appointment.setDescription(descriptionTextField.getText());
            appointment.setLocation(locationTextField.getText());
            appointment.setType(typeTextField.getText());
            LocalTime time = Util.stringToTime(startTimeTextField.getText());
            //appointment.setStartDateTime(LocalDateTime.of(date,time));
            //date = Util.stringToDate(endDateTextField.getText());
            time = Util.stringToTime(endTimeTextField.getText());
            appointment.setCustomer(CustomerDatabaseDao.getInstance().getCustomer(Integer.parseInt(customerIdTextField.getText())));
            appointment.setUser(UserDatabaseDao.getInstance().getUser(Integer.parseInt(userIdTextField.getText())));
            appointment.setContact(contactComboBox.getValue());
            if (contactComboBox.getValue() == null) {
                throw new IllegalArgumentException("Please select an appointment and try again.");
            }
            Appointments.updateAppointment(appointment);
        } catch(IllegalArgumentException ex){
            System.out.println(ex.getMessage());
        }
    }
    public void deleteAppointment(){
        Appointment appointment = appointmentsTable.getSelectionModel().getSelectedItem();
        Appointments.deleteAppointment(appointment);
        //Set label to notify of removal
    }
    private boolean validateAppointment(String title, String description, String location, String type, LocalDateTime start,
                                            LocalDateTime end, Customer customer, User user, Contact contact){
        if(title == null || description == null || location == null || type == null || start == null || end == null
                || customer == null || user == null || contact == null) {
            return false;
        }
        if(validateAppTime(start, end))
            return true;

        return false;
    }
    private boolean validateAppTime(LocalDateTime start, LocalDateTime end){
        ZonedDateTime startZoned = Util.localToZonedTime(start);
        ZonedDateTime endZoned = Util.localToZonedTime(end);
        ZonedDateTime startHQZoned = Util.toHQTimezone(startZoned);
        ZonedDateTime endHQZoned = Util.toHQTimezone(endZoned);

        LocalTime startTime = startHQZoned.toLocalTime();
        LocalTime endTime = endHQZoned.toLocalTime();
        return Util.validateTime(startTime, endTime);

    }

}
