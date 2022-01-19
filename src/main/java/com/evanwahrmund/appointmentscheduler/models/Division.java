package com.evanwahrmund.appointmentscheduler.models;

import com.evanwahrmund.appointmentscheduler.models.Country;

/**
 * Model Object for First Level Division table of database
 */
public class Division {
    /**
     * int representing Division_ID column in database
     */
    private int divId;
    /**
     * String representing Division column in database
     */
    private String name;
    /**
     * Country that this Division is a part of
     */
    private Country country;

    /**
     * Creates first level division from database data
     * @param name String of Division column
     * @param divId int of Division_ID column
     * @param country Country that this Division is a part of
     */
    public Division(String name, int divId, Country country){
        this.name = name;
        this.divId = divId;
        this.country = country;
    }

    /**
     * Gets Country of first level Dvision
     * @return Country of this Division
     */
    public Country getCountry(){
        return country;
    }

    /**
     * Updates Country of first level Division
     * @param country Country to replace country
     */
    public void setCountry(Country country){
        this.country = country;
    }
    /**
     * Gets name of first level Division
     * @return String representing Division
     */
    public String getName(){
        return name;
    }
    /**
     * Updates name of first level division
     * @param name String to replace name
     */
    public void setName(String name){
        this.name = name;
    }

    /**
     * Gets Id of this first level division
     * @return int representing Division_ID
     */
    public int getId() { return divId; }

    /**
     * Updates Id of first level division
     * @param divId int to replace divId
     */
    public void setId(int divId) { this.divId = divId; }

    /**
     * Override of toString() returns Division name
     * @return String name
     */
    @Override
    public String toString(){
        return name;
    }

}
