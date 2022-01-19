package com.evanwahrmund.appointmentscheduler.interfaces;

import com.evanwahrmund.appointmentscheduler.models.Country;
import com.evanwahrmund.appointmentscheduler.models.Division;
import javafx.collections.ObservableList;

/**
 * Interface representing read only data operations
 */
public interface ReadyOnlyDao {
    /**
     * Gets list of all Countries
     * @return ObservableList of all countries
     */
    ObservableList<Country> getAllCountries();

    /**
     * Gets list of all first level divisions
     * @return ObservableList of all first level divisions
     */
    ObservableList<Division> getAllDivisions();

    /**
     * Gets Country with given id
     * @param id int id to search for
     * @return Country with given id, null otherwise
     */
    Country getCountry(int id);

    /**
     * Gets first level division with given id
     * @param id int id to search for
     * @return Division with givin id, null otherwise
     */
    Division getDivision(int id);
}
