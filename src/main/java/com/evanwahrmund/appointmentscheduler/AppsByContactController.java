package com.evanwahrmund.appointmentscheduler;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;

public class AppsByContactController {
    @FXML private TableView<Appointment> contactSchedulesTable;
    @FXML private TableColumn<Appointment, Integer> appIdCol;
    @FXML private TableColumn<Appointment, String> titleCol;
    @FXML private TableColumn<Appointment, String> typeCol;
    @FXML private TableColumn<Appointment, String> descriptionCol;
    @FXML private TableColumn<Appointment, ZonedDateTime> startCol;
    @FXML private TableColumn<Appointment, ZonedDateTime> endCol;
    @FXML private TableColumn<Appointment, Customer> cusIdCol;
    @FXML private ComboBox<Contact> contactComboBox;

    public void initialize(){
        initializeTable();
        contactComboBox.setItems(ContactDatabaseDao.getInstance().getAllContacts());
        contactComboBox.setConverter(new StringConverter<Contact>() {
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
        });
        contactComboBox.setOnAction(event -> {
            contactSchedulesTable.setItems(ReportsDao.getInstance().getAppsByContact(contactComboBox.getValue()));
        });
    }

    private void initializeTable() {
        appIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        startCol.setCellValueFactory( cell -> new ReadOnlyObjectWrapper(Util.formatDateTime(cell.getValue().getStartDateTime())));
        endCol.setCellValueFactory( cell -> new ReadOnlyObjectWrapper(Util.formatDateTime(cell.getValue().getEndDateTime())));
        cusIdCol.setCellValueFactory(cell -> new ReadOnlyObjectWrapper(cell.getValue().getCustomer().getId()));
    }
}
