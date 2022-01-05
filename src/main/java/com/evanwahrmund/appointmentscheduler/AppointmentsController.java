package com.evanwahrmund.appointmentscheduler;

import java.time.*;
import java.util.Locale;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class AppointmentsController {

    @FXML private TableView<Appointment> appointmentsTable;
    @FXML private TableColumn<Appointment, Integer> idCol;
    @FXML private TableColumn<Appointment, String> titleCol;
    @FXML private TableColumn<Appointment, String> typeCol;
    @FXML private TableColumn<Appointment, String> descriptionCol;
    @FXML private TableColumn<Appointment, String> locationCol;
    @FXML private TableColumn<Appointment, Contact> contactCol;
    @FXML private TableColumn<Appointment, ZonedDateTime> startDateTimeCol;
    @FXML private TableColumn<Appointment, ZonedDateTime> endDateTimeCol;
    @FXML private TableColumn<Appointment, Customer> customerCol;


    @FXML private TextField idTextField;
    @FXML private TextField descriptionTextField;
    @FXML private TextField titleTextField;
    @FXML private TextField typeTextField;
    @FXML private TextField locationTextField;
    @FXML private ComboBox<LocalTime> startTimeComboBox;
    @FXML private ComboBox<LocalTime> endTimeComboBox;
    @FXML private DatePicker startDatePicker;
    @FXML private DatePicker endDatePicker;
    @FXML private TextField userIdTextField;
    @FXML private TextField customerIdTextField;
    @FXML private ComboBox<Contact> contactComboBox;
    @FXML private Label deleteLabel;

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
        startDatePicker.setValue(LocalDate.now());
        appointmentsTable.setItems(Appointments.getAppointments());
        resetButton.setOnAction(event -> resetFields());
        addButton.setOnAction(event -> createAppointment());
        modifyButton.setOnAction(event -> modifyAppointment());
        saveButton.setOnAction(event -> updateAppointment());
        deleteButton.setOnAction(event -> deleteAppointment());
        populateTimeComboBoxes();
    }

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

    }
    public void resetFields(){
        idTextField.clear();
        titleTextField.clear();
        descriptionTextField.clear();
        locationTextField.clear();
        typeTextField.clear();
        startTimeComboBox.setValue(null);
        endTimeComboBox.setValue(null);
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
        LocalTime startTime = startTimeComboBox.getValue();
        LocalTime endTime = endTimeComboBox.getValue();
        ZonedDateTime start = ZonedDateTime.of(LocalDateTime.of(startDate, startTime), ZoneId.of("UTC-05:00"));
        start = start.withZoneSameLocal(ZoneId.of("UTC-00:00"));
        ZonedDateTime end = ZonedDateTime.of(LocalDateTime.of(endDate, endTime), ZoneId.of("UTC-05:00"));
        end = end.withZoneSameLocal(ZoneId.of("UTC-00:00"));
        Customer customer = Customers.getCustomer(Integer.parseInt(customerIdTextField.getText()));
        User user = Users.getUser(Integer.parseInt(userIdTextField.getText()));
        Contact contact = contactComboBox.getValue();
        /*if (validateAppointment(title, description, location, type, start, end, customer, user, contact)) {
            Appointment appointment = new Appointment(title, description, location, type, start, end, customer, user, contact);
            Appointments.createAppointment(appointment);
            resetFields();
        }
        else{
            //notify that appointment was not created
        }*/
    }
    public void modifyAppointment(){
        Appointment appointment = appointmentsTable.getSelectionModel().getSelectedItem();
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
    public void updateAppointment() {
        try {
            Appointment appointment = appointmentsTable.getSelectionModel().getSelectedItem();
            appointment.setTitle(titleTextField.getText());
            appointment.setDescription(descriptionTextField.getText());
            appointment.setLocation(locationTextField.getText());
            appointment.setType(typeTextField.getText());

            LocalDateTime start = LocalDateTime.of(startDatePicker.getValue(), startTimeComboBox.getValue());
            ZonedDateTime startZoned = ZonedDateTime.of(start, ZoneId.of("UTC-05:00"));
            startZoned = startZoned.withZoneSameInstant(ZoneId.of("UTC-00:00"));
            appointment.setStartDateTime(startZoned);

            LocalDateTime end = LocalDateTime.of(endDatePicker.getValue(), endTimeComboBox.getValue());
            ZonedDateTime endZoned = ZonedDateTime.of(end, ZoneId.of("UTC-05:00"));
            endZoned = endZoned.withZoneSameInstant(ZoneId.of("UTC-00:00"));
            appointment.setEndDateTime(endZoned);

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
        deleteLabel.setText(appointment.getTitle() + " has been successfully deleted");
    }/*
    private boolean validateAppointment(String title, String description, String location, String type, ZonedDateTime start,
                                        ZonedDateTime end, Customer customer, User user, Contact contact){
        if(title == null || description == null || location == null || type == null || start == null || end == null
                || customer == null || user == null || contact == null) {
            return false;
        }
        if(validateAppTime(start, end))
            return true;

        return false;
    }/*
    private boolean validateAppTime(ZonedDateTime start, ZonedDateTime end){
        ZonedDateTime startZoned = Util.localToZonedTime(start);
        ZonedDateTime endZoned = Util.localToZonedTime(end);
        ZonedDateTime startHQZoned = Util.toHQTimezone(startZoned);
        ZonedDateTime endHQZoned = Util.toHQTimezone(endZoned);

        LocalTime startTime = startHQZoned.toLocalTime();
        LocalTime endTime = endHQZoned.toLocalTime();
        return Util.validateTime(startTime, endTime);

    }*/

}
