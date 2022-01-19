package com.evanwahrmund.appointmentscheduler.models;

import java.sql.SQLException;

import com.evanwahrmund.appointmentscheduler.daos.AppointmentDatabaseDao;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Represents all appointments in the database.
 * Used to set values of appointment tables and observe changes.
 * Ensures consistent display of data between database and application.
 * Initializes list of appointments when first called by Application
 */
public class Appointments {

    /**
     * ObservableList representing all appointments in the database.
     * Used to set values of appointment TableViews that can be observed
     */
    private static ObservableList<Appointment> appointments = FXCollections.observableArrayList();

    static {
        appointments = AppointmentDatabaseDao.getInstance().getAllAppointments();
    }

    /**
     * Gets all Appointments
     * @return ObservableList of all Appointments
     */
    public static ObservableList<Appointment> getAppointments() {
        return appointments;
    }

    /**
     * Selects Appointment with given id
     * Ensures any appointment selected is associated with its database representation
     * @param id int to search for
     * @return Appointment with given id, null otherwise
     */
    public static Appointment getAppointment(int id) {
        for (Appointment appointment : appointments) {
            if (appointment.getId() == id)
                return appointment;
        }
        return null;
    }

    /**
     * Adds given Appointment to database and list of Appointments
     * @param appointment Appointment to be added
     * @throws SQLException if any errors occur while adding to the database
     */
    public static void createAppointment(Appointment appointment) throws SQLException {
        AppointmentDatabaseDao.getInstance().createAppointment(appointment);
        appointments.add(appointment);
    }

    /**
     * Updates given Appointment in database and in list of Appointments
     * @param appointment Appointment to be updated
     * @throws SQLException if any errors occur while updating in database
     */
    public static void updateAppointment(Appointment appointment) throws SQLException {
        AppointmentDatabaseDao.getInstance().updateAppointment(appointment);
        appointments.set(appointments.indexOf(appointment), appointment);

    }

    /**
     * Deletes given Appointment from database and list of Appintments
     * @param appointment Appointment to be deleted
     * @throws SQLException if any errors occur while deleting in database
     */
    public static void deleteAppointment(Appointment appointment) throws SQLException {
        AppointmentDatabaseDao.getInstance().deleteAppointment(appointment);
        appointments.remove(appointment);

    }

}