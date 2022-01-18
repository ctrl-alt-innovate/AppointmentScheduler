package com.evanwahrmund.appointmentscheduler;

import java.sql.SQLException;

import com.evanwahrmund.appointmentscheduler.daos.AppointmentDatabaseDao;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Appointments {

    /**
     * represents a collection of appointments retrieved from database
     */
    private static ObservableList<Appointment> appointments = FXCollections.observableArrayList();
    static {
        appointments = AppointmentDatabaseDao.getInstance().getAllAppointments();
    }
    /**
     * Retrieves instance variable of appointments. Observed by TableViews for updates.<p>
     * Prevents excessive calls to database
     * @return ObservableList of appointments representing appointments in database
     */
    public static ObservableList<Appointment> getAppointments(){
        return appointments;
    }
    public static Appointment getAppointment(int id) {
        for (Appointment appointment : appointments) {
            if (appointment.getId() == id)
                return appointment;
        }
        return null;
    }
    public static void createAppointment(Appointment appointment) throws SQLException {
        AppointmentDatabaseDao.getInstance().createAppointment(appointment);
        appointments.add(appointment);
    }
    public static void updateAppointment(Appointment appointment) throws SQLException{
        AppointmentDatabaseDao.getInstance().updateAppointment(appointment);
        appointments.set(appointments.indexOf(appointment), appointment);

    }
    public static void deleteAppointment(Appointment appointment) throws SQLException{
        AppointmentDatabaseDao.getInstance().deleteAppointment(appointment);
        appointments.remove(appointment);

    }
    /*public static ObservableList<Appointment> getAppsByMonth(LocalDateTime begOfMonth){
        String monthName = begOfMonth.getMonth().toString().toUpperCase();
        //ObservableList<Appointment> appsByMonth = AppointmentDatabaseDao.getInstance().getAppsByMonth(monthName);
        return appsByMonth;
    }*/
}
