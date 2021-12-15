package com.evanwahrmund.appointmentscheduler;

public class Division {

    private String name;
    private int divId;
    private Country country;

    public Division(String name, int divId, Country country){
        this.name = name;
        this.divId = divId;
        this.country = country;
    }
    public Country getCountry(){
        return country;
    }
    public void setCountry(Country country){
        this.country = country;
    }
    /**
     * Gets name of this first level Division
     * @return String representing name
     */
    public String getName(){
        return name;
    }
    /**
     * Sets name for this first level division
     * @param name String to update current first level division
     */
    public void setName(String name){
        this.name = name;
    }

    public int getId() { return divId; }
    public void setId(int divId) { this.divId = divId; }
    @Override
    public String toString(){
        return name;
    }

}
