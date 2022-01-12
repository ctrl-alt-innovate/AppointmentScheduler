package com.evanwahrmund.appointmentscheduler.interfaces;

import com.evanwahrmund.appointmentscheduler.Appointment;
import javafx.collections.ObservableList;

import java.sql.SQLException;

public interface AppointmentDao {

    ObservableList<Appointment> getAllAppointments();
    Appointment getAppointment(int id);
    void createAppointment(Appointment appointment) throws SQLException;
    void updateAppointment(Appointment appointment) throws SQLException;
    void deleteAppointment(Appointment appointment) throws SQLException;
}
