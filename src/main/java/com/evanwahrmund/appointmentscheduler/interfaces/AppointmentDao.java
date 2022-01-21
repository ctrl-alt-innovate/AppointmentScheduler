package com.evanwahrmund.appointmentscheduler.interfaces;

import com.evanwahrmund.appointmentscheduler.models.Appointment;
import javafx.collections.ObservableList;

/**
 * interface representing Appointment operations
 */
public interface AppointmentDao {
    /**
     * Gets list of all Appointments
     * @return ObservableList of all Appointments
     */
    ObservableList<Appointment> getAllAppointments();

    /**
     * Gets Appointment with given id
     * @param id int id to search for
     * @return Appointment with given id, null otherwise
     */
    Appointment getAppointment(int id);

    /**
     * Creates new Appointment
     * @param appointment Appointment to be created
     * @throws Exception if any errors occur while creating Appointment
     */
    void createAppointment(Appointment appointment) throws Exception;

    /**
     * Updates given Appointment
     * @param appointment Appointment to be updated
     * @throws Exception if any errors occur while updating Appointment
     */
    void updateAppointment(Appointment appointment) throws Exception;

    /**
     * Deletes given Appointment
     * @param appointment Appointment to be deleted
     * @throws Exception if any erors occur while deleting Appointment
     */
    void deleteAppointment(Appointment appointment) throws Exception;
}
