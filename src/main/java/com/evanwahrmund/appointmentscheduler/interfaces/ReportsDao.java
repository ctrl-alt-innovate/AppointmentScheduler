package com.evanwahrmund.appointmentscheduler.interfaces;

import com.evanwahrmund.appointmentscheduler.models.Appointment;
import com.evanwahrmund.appointmentscheduler.models.Contact;
import com.evanwahrmund.appointmentscheduler.util.ReportVal;
import javafx.collections.ObservableList;

public interface ReportsDao {
    ObservableList<ReportVal> getAppsByTypeAndMonth();

    ObservableList<Appointment> getAppsByContact(Contact contact);

    ObservableList<ReportVal> getAppsByCountry();

    ObservableList<ReportVal> getAppsByFirstLevelDiv();

    ObservableList<ReportVal> getCusByCountry();

    ObservableList<ReportVal> getCusByFirstLevelDiv();
}
