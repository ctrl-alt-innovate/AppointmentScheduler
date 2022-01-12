package com.evanwahrmund.appointmentscheduler.interfaces;

import com.evanwahrmund.appointmentscheduler.Country;
import com.evanwahrmund.appointmentscheduler.Division;
import javafx.collections.ObservableList;

/**
 * Interface representing a data access object used to retrieve read only data
 */
public interface ReadyOnlyDao {
    ObservableList<Country> getAllCountries();
    ObservableList<Division> getAllDivisions();
    Country getCountry(int id);
    Division getDivision(int id);
}
