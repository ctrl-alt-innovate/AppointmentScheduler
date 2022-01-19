package com.evanwahrmund.appointmentscheduler.models;

import com.evanwahrmund.appointmentscheduler.daos.ReadOnlyDatabaseDao;
import com.evanwahrmund.appointmentscheduler.models.Country;
import javafx.collections.ObservableList;

/**
 * Represents all Countries in the database.
 * Initializes list of Countries when first called by application
 */
public class Countries {
    /**
     * ObservableList of all Countries in the database
     */
    private static ObservableList<Country> countries;
    static {
        countries = ReadOnlyDatabaseDao.getInstance().getAllCountries();

    }

    /**
     * Gets list of all Countries
     * @return ObservableList of all Countries
     */
    public static ObservableList<Country> getCountries(){
        return countries;
    }

    /**
     * Selects Country with given id
     * @param id int id of Country to search for
     * @return Country with given id, null otherwise
     */
    public static Country getCountry(int id){
        for(Country country: countries){
            if(country.getId() == id)
                return country;
        }
        return null;
    }


}
