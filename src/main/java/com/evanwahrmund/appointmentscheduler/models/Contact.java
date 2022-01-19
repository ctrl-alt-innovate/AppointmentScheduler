package com.evanwahrmund.appointmentscheduler.models;

/**
 * Model object for Contact from Contacts table in database
 */
public class Contact {
    /**
     * int representing Contatct_ID column
     */
    private int id;
    /**
     * String representing Contact_Name column
     */
    private String name;
    /**
     * String representing Email column
     */
    private String email;

    /**
     * Creates Contact from database data
     * @param id int of Contact_ID col
     * @param name String of Contact_Name col
     * @param email String of Email col
     */
    public Contact(int id, String name, String email){
        this.id = id;
        this.name = name;
        this.email = email;
    }

    /**
     * Gets Id of Contact
     * @return int representing Contact_ID
     */
    public int getId(){
        return id;
    }

    /**
     * Updates Id of Contact
     * @param id int to replace id
     */
    public void setId(int id){
        this.id = id;
    }

    /**
     * Gets Name of Contact
     * @return String representing Contact_Name
     */
    public String getName(){
        return name;
    }

    /**
     * Updats Name of Contact
     * @param name String to replace name
     */
    public void setName(String name){
        this.name = name;
    }

    /**
     * Gets Email of Contact
     * @return String reresenting Email
     */
    public String getEmail(){
        return email;
    }

    /**
     * Updates Email of Contact
     * @param email String to replace email
     */
    public void setEmail(String email){
        this.email = email;
    }

    /**
     * Override of toString() returns id and name
     * @return String containing id and name
     */
    @Override
    public String toString(){
        return id + " - " + name;
    }
}
