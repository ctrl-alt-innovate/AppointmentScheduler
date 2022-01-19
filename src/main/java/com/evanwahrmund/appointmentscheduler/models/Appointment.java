package com.evanwahrmund.appointmentscheduler.models;

import java.time.ZonedDateTime;

public class Appointment {

    private int id = 0;
    private String title;
    private String description;
    private String location;
    private String type;
    private ZonedDateTime startDateTime;
    private ZonedDateTime endDateTime;
    private User user;
    private Contact contact;
    private Customer customer;

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
