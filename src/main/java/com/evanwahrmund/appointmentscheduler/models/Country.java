package com.evanwahrmund.appointmentscheduler.models;

/**
 * Model object representing Country table of database
 */
public class Country {
    /**
     * int representing Country_ID column
     */
    private int id;
    /**
     * String representing Country column
     */
    private String name;

    /**
     * Creates Country from database data
     * @param name String of Country column
     * @param id int of Country_ID column
     */
    public Country(String name, int id){
        this.name = name;
        this.id = id;
    }

    /**
     * Gets Id of Country
     * @return int representing Country_ID
     */
    public int getId(){
        return id;
    }

    /**
     * Updates Id of Country
     * @param id to replace id
     */
    public void setId(int id){
        this.id = id;
    }

    /**
     * Gets name of Country
     * @return String representing Country
     */
    public String getName(){
        return name;
    }

    /**
     * Updates name of Country
     * @param name String to replace name
     */
    public void setName(String name){
        this.name = name;
    }

    /**
     * Ovveride of toString() returns Country name
     * @return String name
     */
    @Override
    public String toString(){
        return name;
    }
}
