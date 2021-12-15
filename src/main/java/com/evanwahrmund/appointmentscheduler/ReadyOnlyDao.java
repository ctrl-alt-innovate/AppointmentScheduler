package com.evanwahrmund.appointmentscheduler;

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
