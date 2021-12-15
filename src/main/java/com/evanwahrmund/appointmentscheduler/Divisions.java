package com.evanwahrmund.appointmentscheduler;

import javafx.collections.ObservableList;

public class Divisions {

    private static ObservableList<Division> divisions;
    static {
        divisions = ReadOnlyDatabaseDao.getInstance().getAllDivisions();
    }

    public static ObservableList<Division> getDivisions(){
        return divisions;
    }
    public static Division getDivision(int id){
        for(Division division: divisions){
            if(division.getId() == id)
                return division;
        }
        return null;
    }

}
