package com.evanwahrmund.appointmentscheduler.controllers;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.function.Predicate;

import com.evanwahrmund.appointmentscheduler.daos.ReportsDao;
import com.evanwahrmund.appointmentscheduler.models.*;
import com.evanwahrmund.appointmentscheduler.util.Util;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * Controller for the FXML file: apps_by_contact_report.fxml.
 * Displays List of Appointments by Contact based on User selection
 * <p>
 *  DISCUSSION OF LAMBDA 2: initialize()
 */
public class AppsByContactController {
    /*
     * TableView of all Appointments, to be filtered by contact
     */
    @FXML private TableView<Appointment> contactSchedulesTable;
    /**
     * col for id
     */
    @FXML private TableColumn<Appointment, Integer> appIdCol;
    /**
     * col for title
     */
    @FXML private TableColumn<Appointment, String> titleCol;
    /**
     * col for type
     */
    @FXML private TableColumn<Appointment, String> typeCol;
    /**
     * col for description
     */
    @FXML private TableColumn<Appointment, String> descriptionCol;
    /**
     * col for start time
     */
    @FXML private TableColumn<Appointment, ZonedDateTime> startCol;
    /**
     * col for end time
     */
    @FXML private TableColumn<Appointment, ZonedDateTime> endCol;
    /**
     * col for customer id
     */
    @FXML private TableColumn<Appointment, Customer> cusIdCol;
    /**
     * ComboBox of Contacts for User to choose from
     */
    @FXML private ComboBox<Contact> contactComboBox;

    /**
     * Assigns all contacts to contactComboBox and sets event handler for contactComboBox.
     * <p>
     * DISCUSSION OF LAMMDA 2: pred is a predicated used to filter the appiontments list based on contact choice of the
     * contactComboBox. The filtered list is created and displayed everytime a contact choice is made.  This ensures that
     * no new lists have to be created and enables fast displaying of data.
     */
    public void initialize(){
        initializeTable();
        contactComboBox.setItems(Contacts.getContacts());

        Predicate<Appointment> pred = app -> ReportsDao.getInstance().getAppsByContact(contactComboBox.getValue()).contains(app);

        contactComboBox.setOnAction(event -> {
            FilteredList<Appointment> byContact = new FilteredList<Appointment>(Appointments.getAppointments(), pred);
            contactSchedulesTable.setItems(byContact);
        });
    }

    /**
     * Initializes table columns
     */
    private void initializeTable() {
        appIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        startCol.setCellValueFactory( cell -> new ReadOnlyObjectWrapper(Util.formatDateTime(cell.getValue().getStartDateTime()
                .withZoneSameInstant(ZoneId.systemDefault()))));
        endCol.setCellValueFactory( cell -> new ReadOnlyObjectWrapper(Util.formatDateTime(cell.getValue().getEndDateTime()
                .withZoneSameInstant(ZoneId.systemDefault()))));
        cusIdCol.setCellValueFactory(cell -> new ReadOnlyObjectWrapper(cell.getValue().getCustomer().getId()));
    }
}
