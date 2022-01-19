package com.evanwahrmund.appointmentscheduler.models;

import java.time.ZonedDateTime;

/**
 * Model object representing Appointment from Appointments table of database
 */
public class Appointment {
    /**
     * int representing Appointment_ID column
     */
    private int id = 0;
    /**
     * String representing Title column
     */
    private String title;
    /**
     * String representing Description column
     */
    private String description;
    /**
     * String representing Location column
     */
    private String location;
    /**
     * String representing Type column
     */
    private String type;
    /**
     * ZonedDateTime reprsenting Start column
     */
    private ZonedDateTime startDateTime;
    /**
     * ZonedDateTime representing End column
     */
    private ZonedDateTime endDateTime;
    /**
     * User of the Appointment
     */
    private User user;
    /**
     * Contact of the Appointment
     */
    private Contact contact;
    /**
     * Customer of the Appointment
     */
    private Customer customer;

    /**
     * Creates an Appointment from database data
     * @param id int id of Appointment_ID col
     * @param title String of Title col
     * @param description String of Description col
     * @param location String of Location col
     * @param type String of Type col
     * @param startDateTime ZonedDateTime of Start col
     * @param endDateTime ZonedDateTime of End col
     * @param customer Customer of Appointment
     * @param user User of Appointment
     * @param contact Contact of Appointment
     */
    public Appointment(int id, String title, String description, String location, String type, ZonedDateTime startDateTime,
                       ZonedDateTime endDateTime, Customer customer, User user, Contact contact){
        this.id = id;
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.customer = customer;
        this.user = user;
        this.contact = contact;

    }

    /**
     * Creates new Appointment without assigning id.
     * Used by application to create new Appointment before getting generated id from database
     * @param title String title of new appointment
     * @param description String description of new appointment
     * @param location String location of new appointment
     * @param type String type of new appointment
     * @param startDateTime ZonedDateTime start of new appointment
     * @param endDateTime ZonedDateTime end of new appointment
     * @param customer Customer of new appointment
     * @param user User of new appointment
     * @param contact Contact of new appointment
     */
    public Appointment(String title, String description, String location, String type, ZonedDateTime startDateTime,
                       ZonedDateTime endDateTime, Customer customer, User user, Contact contact){
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.customer = customer;
        this.user = user;
        this.contact = contact;

    }

    /**
     * Gets Id of Appointment
     * @return int representing Appointment_ID
     */
    public int getId(){
        return id;
    }

    /**
     * Updates Id of Appointment
     * @param id int to replace id
     */
    public void setId(int id){
        this.id = id;
    }

    /**
     * Gets title of Appointment
     * @return String representing Title
     */
    public String getTitle(){
        return title;
    }

    /**
     * Updates title of Appointment
     * @param title String to replace title
     */
    public void setTitle(String title){
        this.title = title;
    }

    /**
     * Gets description of Appointment
     * @return String representing Description
     */
    public String getDescription(){
        return description;
    }

    /**
     * Updates description of Appointment
     * @param description String to replace description
     */
    public void setDescription(String description){
        this.description = description;
    }

    /**
     * Gets location of Appointment
     * @return String representing Location
     */
    public String getLocation(){
        return location;
    }

    /**
     * Updates location of Appointment
     * @param location String to replace location
     */
    public void setLocation(String location){
        this.location = location;
    }

    /**
     * Gets Start time of Appointment
     * @return ZonedDateTime representing Start
     */
    public ZonedDateTime getStartDateTime(){
        return startDateTime;
    }

    /**
     * Updates Start time of Appointment
     * @param startDateTime ZonedDateTime to replace startDateTime
     */
    public void setStartDateTime(ZonedDateTime startDateTime){
        this.startDateTime = startDateTime;
    }

    /**
     * Gets End time of Appointment
     * @return ZonedDateTime representing End
     */
    public ZonedDateTime getEndDateTime(){
        return endDateTime;
    }

    /**
     * Updates End time of Appointment
     * @param endDateTime ZonedDateTime to replace endDateTime
     */
    public void setEndDateTime(ZonedDateTime endDateTime){
        this.endDateTime = endDateTime;
    }

    /**
     * Gets User of Appointment
     * @return User of Appointment
     */
    public User getUser(){
        return user;
    }

    /**
     * Updates User of Appointment
     * @param user User to replace user
     */
    public void setUser(User user){
        this.user = user;
    }

    /**
     * Gets Contact of Appointment
     * @return Contact of Appointment
     */
    public Contact getContact(){
        return contact;
    }

    /**
     * Updates Contact of Appointment
     * @param contact Contact to replace contact
     */
    public void setContact(Contact contact){
        this.contact = contact;
    }

    /**
     * Gets Customer of Appointment
     * @return Customer of Appointment
     */
    public Customer getCustomer(){
        return customer;
    }

    /**
     * Updates Customer of Appointment
     * @param customer Customer to replace customer
     */
    public void setCustomer(Customer customer){
        this.customer = customer;
    }

    /**
     * Gets type of Appointment
     * @return String representing Type
     */
    public String getType(){
        return type;
    }

    /**
     * Updates type of Appointment
     * @param type String to replace type
     */
    public void setType(String type){
        this.type = type;
    }
}
