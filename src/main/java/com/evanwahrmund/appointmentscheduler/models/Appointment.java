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
     *
     * @param id
     * @param title
     * @param description
     * @param location
     * @param type
     * @param startDateTime
     * @param endDateTime
     * @param customer
     * @param user
     * @param contact
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
    public int getId(){
        return id;
    }
    public void setId(int id){
        this.id = id;
    }
    public String getTitle(){
        return title;
    }
    public void setTitle(String title){
        this.title = title;
    }
    public String getDescription(){
        return description;
    }
    public void setDescription(String description){
        this.description = description;
    }
    public String getLocation(){
        return location;
    }
    public void setLocation(String location){
        this.location = location;
    }
    public ZonedDateTime getStartDateTime(){
        return startDateTime;
    }
    public void setStartDateTime(ZonedDateTime startDateTime){
        this.startDateTime = startDateTime;
    }
    public ZonedDateTime getEndDateTime(){
        return endDateTime;
    }
    public void setEndDateTime(ZonedDateTime endDateTime){
        this.endDateTime = endDateTime;
    }
    public User getUser(){
        return user;
    }
    public void setUser(User user){
        this.user = user;
    }
    public Contact getContact(){
        return contact;
    }
    public void setContact(Contact contact){
        this.contact = contact;
    }
    public Customer getCustomer(){
        return customer;
    }
    public void setCustomer(Customer customer){
        this.customer = customer;
    }
    public String getType(){
        return type;
    }
    public void setType(String type){
        this.type = type;
    }
}
