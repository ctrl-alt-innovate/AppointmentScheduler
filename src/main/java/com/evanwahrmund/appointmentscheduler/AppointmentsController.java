package com.evanwahrmund.appointmentscheduler;

import java.sql.SQLException;
import java.sql.SQLWarning;
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
    private Boolean startChanged = false;
    private Boolean endChanged = false;

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
        loadTestData();
        startDatePicker.setOnAction(event -> startChanged = false);
        startTimeComboBox.valueProperty().addListener((obs, old, newVal) -> {
            if(startTimeComboBox.getItems().indexOf(newVal) >= startTimeComboBox.getItems().indexOf(LocalTime.of(0,0))
                    && startChanged == false) {
                startDatePicker.setValue(startDatePicker.getValue().plusDays(1));
                startChanged = true;
            }else if(startTimeComboBox.getItems().indexOf(newVal) >= startTimeComboBox.getItems().indexOf(LocalTime.of(0,0))
                    && startChanged == true){
                System.out.println("NO change");
            }else if(startTimeComboBox.getItems().indexOf(newVal) < startTimeComboBox.getItems().indexOf(LocalTime.of(0,0))
                    && startChanged == false)
                System.out.println("NO change");
            else {
                startDatePicker.setValue(startDatePicker.getValue().minusDays(1));
            }
        });
        endDatePicker.setOnAction(event -> endChanged = false);
        endTimeComboBox.valueProperty().addListener((obs, old, newVal) -> {
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
        });

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
        try{
            String title = titleTextField.getText();
            String description = descriptionTextField.getText();
            String location = locationTextField.getText();
            String type = typeTextField.getText();


            LocalDate startDate = startDatePicker.getValue();
            LocalDate endDate = endDatePicker.getValue();
            LocalTime startTime = startTimeComboBox.getValue();
            LocalTime endTime = endTimeComboBox.getValue();

            if(title.isBlank() || description.isBlank() || location.isBlank() || type.isBlank() || startDate == null ||
                    endDate == null || startTime == null || endTime == null){
                throw new IllegalArgumentException("Fields Can not be left blank");
            }

            Customer customer = Customers.getCustomer(Integer.parseInt(customerIdTextField.getText()));
            User user = Users.getUser(Integer.parseInt(userIdTextField.getText()));
            Contact contact = contactComboBox.getValue();

            if(customer == null || user == null || contact == null){
                throw new IllegalArgumentException("Please select a valid contact, customer, and user.");
            }

            ZonedDateTime start = ZonedDateTime.of(LocalDateTime.of(startDate, startTime), ZoneId.systemDefault());
            start = start.withZoneSameInstant(ZoneId.of("UTC"));
            ZonedDateTime end = ZonedDateTime.of(LocalDateTime.of(endDate, endTime), ZoneId.systemDefault());
            end = end.withZoneSameInstant(ZoneId.of("UTC"));
            for(Appointment app: Appointments.getAppointments()){
                /*if(start.isAfter(app.getStartDateTime()) && start.isBefore(app.getEndDateTime())){
                    throw new IllegalArgumentException("Appointments can not overlap.");
                }
                if(end.isAfter(app.getStartDateTime()) && end.isBefore(app.getEndDateTime())){
                    throw new IllegalArgumentException("Appointments can not overlap.");
                }*/
                if(start.isEqual(app.getStartDateTime()) && end.isEqual(app.getEndDateTime())){
                    throw new IllegalArgumentException("Appointment already exists for this time");
                }
                if(start.isEqual(app.getStartDateTime()) && end.isBefore(app.getEndDateTime())){
                    throw new IllegalArgumentException("Can not schedule overlapping appointments");
                }
                if(start.isAfter(app.getStartDateTime()) && end.isEqual(app.getEndDateTime())){
                    throw new IllegalArgumentException("Can not schedule overlapping appointments");
                }
                if(start.isAfter(app.getStartDateTime()) && end.isBefore(app.getEndDateTime())){
                    throw new IllegalArgumentException("Can not schedule overlapping appointments");
                }
                if(start.isBefore(app.getStartDateTime()) && (end.isAfter(app.getStartDateTime()) && end.isBefore(app.getEndDateTime()))){
                    throw new IllegalArgumentException("Can not schedule overlapping appointments");
                }
                if(end.isAfter(app.getEndDateTime()) && (start.isAfter(app.getStartDateTime())&& start.isBefore(app.getEndDateTime()))){
                    throw new IllegalArgumentException("Can not schedule overlapping appointments");
                }

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
    public void updateAppointment() {
        Appointment appointment = appointmentsTable.getSelectionModel().getSelectedItem();
        if(appointment == null){
            Alert error = new Alert(Alert.AlertType.ERROR, "Please select an appointment to update.");
            error.setHeaderText("Appointment Update Error");
            error.show();
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

        } catch(IllegalArgumentException | SQLException | NullPointerException ex){
            Alert error = new Alert(Alert.AlertType.ERROR,"Error updating Appointment ID: " + appointment.getId() + " - " +
                    appointment.getType() + "\n" + ex.getMessage());
            error.setHeaderText("Appointment Update Error");
            error.show();
            System.out.println(ex.getMessage());
        }
    }
    public void deleteAppointment(){
        try {
            Appointment appointment = appointmentsTable.getSelectionModel().getSelectedItem();
            if(appointment == null){
                Alert alert = new Alert(Alert.AlertType.ERROR, "No Appointment selected to delete.");
                alert.setHeaderText("Appointment Deletion Error");
                alert.show();
            }
            Appointments.deleteAppointment(appointment);
            //deleteLabel.setText(appointment.getTitle() + " has been successfully deleted");
            Alert confirm = new Alert(Alert.AlertType.INFORMATION, "Appointment ID: " + appointment.getId() + " - " +
                    appointment.getType() + " deleted successfully.");
            confirm.setHeaderText("Appointment Deleted");
            confirm.show();
            //ALERT HERE NOTIFY DELETEION DISPLAY ID AND TYPE
        } catch (SQLException ex){
            Alert error = new Alert(Alert.AlertType.ERROR, "Error deleting Appointment" + "\n" + ex.getMessage());
            error.setHeaderText("Appointment Deletion Error");
            error.show();

        }
    }
    private void loadTestData(){
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
    }
}