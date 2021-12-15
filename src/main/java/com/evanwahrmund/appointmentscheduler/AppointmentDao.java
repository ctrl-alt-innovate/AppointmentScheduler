package com.evanwahrmund.appointmentscheduler;

import javafx.collections.ObservableList;

public interface AppointmentDao {

    ObservableList<Appointment> getAllAppointments();
    Appointment getAppointment(int id);
    boolean createAppointment(Appointment appointment);
    boolean updateAppointment(Appointment appointment);
    boolean deleteAppointment(Appointment appointment);
}
