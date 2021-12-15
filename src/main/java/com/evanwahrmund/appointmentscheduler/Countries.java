package com.evanwahrmund.appointmentscheduler;

import javafx.collections.ObservableList;

public class Countries {

    private static ObservableList<Country> countries;
    static {
        countries = ReadOnlyDatabaseDao.getInstance().getAllCountries();

    }

    public static ObservableList<Country> getCountries(){
        return countries;
    }
    public static Country getCountry(int id){
        for(Country country: countries){
            if(country.getId() == id)
                return country;
        }
        return null;
    }


}
