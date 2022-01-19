package com.evanwahrmund.appointmentscheduler.models;

import com.evanwahrmund.appointmentscheduler.models.Division;

/**
 * Model object for Customer from Customers table of database
 */
public class Customer {
    /**
     * int representing Customer_ID column
     */
    private int id = 0;
    /**
     * String representing Customer_Name column
     */
    private String name;
    /**
     * String representing Phone column
     */
    private String phone;
    /**
     * String representing Address column
     */
    private String address;
    /**
     * String representing Postal_Code column
     */
    private String postalCode;
    /**
     * First level division where this Customer is located
     */
    private Division division;


    /**
     * Creates new Customer without assigning id.
     * Used by application to create Customer before getting generated id from database
     * @param name String name of new customer
     * @param address String address of new customer
     * @param postalCode String postal code of new customer
     * @param phone String phone number of new customer
     * @param division Diuision where customer is located
     */
    public Customer(String name, String address, String postalCode,  String phone, Division division){
        this.name = name;
        this.address = address;
        this.postalCode = postalCode;
        this.phone = phone;
        this.division = division;
    }

    /**
     * Creates a customer from database data
     * @param id int of Customer_ID column
     * @param name String of Customer_Name column
     * @param address String of Address column
     * @param postalCode String of Postal_Code column
     * @param phone String of Phone column
     * @param division Division where customer is located
     */
    public Customer(int id, String name, String address, String postalCode,  String phone, Division division){
        this.id = id;
        this.name = name;
        this.address = address;
        this.postalCode = postalCode;
        this.phone = phone;
        this.division = division;
    }

    /**
     * Gets Id of Customer
     * @return int represeting Customer_ID
     */
    public int getId(){
        return id;
    }

    /**
     * Gets name of Customer
     * @return String representing Customer_Name
     */
    public String getName(){
        return name;
    }

    /**
     * Gets phone number of Customer
     * @return String representing Phone
     */
    public String getPhone(){
        return phone;
    }

    /**
     * Gets address of Customer
     * @return String representing Address
     */
    public String getAddress(){
        return address;
    }

    /**
     * Gets postal code of Customer
     * @return String representing Postal_Code
     */
    public String getPostalCode(){
        return postalCode;
    }

    /**
     * Gets first level division of customer
     * @return Division where customer is located
     */
    public Division getDivision(){
        return division;
    }

    /**
     * Updates Id of Customer
     * @param id int to replace id
     */
    public void setId(int id){
        this.id = id;
    }

    /**
     * Updates name of Customer
     * @param name String to replace name
     */
    public void setName(String name){
        this.name = name;
    }

    /**
     * Updates phone number of Customer
     * @param phone String to replace phone
     */
    public void setPhone(String phone){
        this.phone = phone;
    }

    /**
     * Updates address of Customer
     * @param address String to replace address
     */
    public void setAddress(String address){
        this.address = address;
    }

    /**
     * Updates postal code of Customer
     * @param postalCode String to replace postalCode
     */
    public void setPostalCode(String postalCode){
        this.postalCode = postalCode;
    }

    /**
     * Updates first level division of Customer
     * @param division Division to replace division
     */
    public void setDivision(Division division){
        this.division = division;
    }

}
