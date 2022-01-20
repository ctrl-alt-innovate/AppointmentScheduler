package com.evanwahrmund.appointmentscheduler.interfaces;

import com.evanwahrmund.appointmentscheduler.models.Appointment;
import com.evanwahrmund.appointmentscheduler.models.Contact;
import com.evanwahrmund.appointmentscheduler.util.ReportVal;
import javafx.collections.ObservableList;

public interface ReportsDao {
    /**
     * Gets Appointments by type and month
     * @return ObservableList of Report values
     */
    ObservableList<ReportVal> getAppsByTypeAndMonth();

    /**
     * Gets Appointments with given Contact
     * @param contact Contact to find appointments for
     * @return ObservableList of Appointments with given contact
     */
    ObservableList<Appointment> getAppsByContact(Contact contact);
    /**
     * Gets Appointments by Country
     * @return ObservableList of Report values
     */
    ObservableList<ReportVal> getAppsByCountry();
    /**
     * Gets Appointments by Division
     * @return ObservbableList of Report values
     */
    ObservableList<ReportVal> getAppsByFirstLevelDiv();
    /**
     * Gets Customers by Country
     * @return ObservableLIst of Report values
     */
    ObservableList<ReportVal> getCusByCountry();
    /**
     * Gets Customers by Division
     * @return ObservableList of Report values
     */
    ObservableList<ReportVal> getCusByFirstLevelDiv();
}
