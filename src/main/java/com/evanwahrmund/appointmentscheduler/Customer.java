package com.evanwahrmund.appointmentscheduler;

import com.evanwahrmund.appointmentscheduler.models.Division;

public class Customer {
    private int id = 0;
    private String name;
    private String phone;
    private String address;
    private String postalCode;
    private Division division;



    public Customer(String name, String address, String postalCode,  String phone, Division division){
        this.name = name;
        this.address = address;
        this.postalCode = postalCode;
        this.phone = phone;
        this.division = division;
    }
    public Customer(int id, String name, String address, String postalCode,  String phone, Division division){
        this.id = id;
        this.name = name;
        this.address = address;
        this.postalCode = postalCode;
        this.phone = phone;
        this.division = division;
    }
    public int getId(){
        return id;
    }
    public String getName(){
        return name;
    }
    public String getPhone(){
        return phone;
    }
    public String getAddress(){
        return address;
    }
    public String getPostalCode(){
        return postalCode;
    }
    public Division getDivision(){
        return division;
    }
    /*public Country getCountry(){
        return country;
    }*/
    public void setId(int id){
        this.id = id;
    }
    public void setName(String name){
        this.name = name;
    }
    public void setPhone(String phone){
        this.phone = phone;
    }
    public void setAddress(String address){
        this.address = address;
    }
    public void setPostalCode(String postalCode){
        this.postalCode = postalCode;
    }
    public void setDivision(Division division){
        this.division = division;
    }
    /*public void setCountry(Country country){
        this.country = country;
    }*/
}
