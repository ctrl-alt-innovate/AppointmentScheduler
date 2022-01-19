package com.evanwahrmund.appointmentscheduler.models;

import com.evanwahrmund.appointmentscheduler.daos.ReadOnlyDatabaseDao;
import com.evanwahrmund.appointmentscheduler.models.Division;
import javafx.collections.ObservableList;

/**
 * Represents all first level divisions in the database
 * Initializes list of all Divisions when first called by the application
 */
public class Divisions {
    /**
     * ObservableList representing all first level divisions in the database
     */
    private static ObservableList<Division> divisions;
    static {
        divisions = ReadOnlyDatabaseDao.getInstance().getAllDivisions();
    }

    /**
     * Gets list of all first level divisions
     * @return ObservableList of all Divisions
     */
    public static ObservableList<Division> getDivisions(){
        return divisions;
    }

    /**
     * Gets first level division with given id
     * @param id int id of Division to search for
     * @return Division with selected id, null otherwise
     */
    public static Division getDivision(int id){
        for(Division division: divisions){
            if(division.getId() == id)
                return division;
        }
        return null;
    }

}
