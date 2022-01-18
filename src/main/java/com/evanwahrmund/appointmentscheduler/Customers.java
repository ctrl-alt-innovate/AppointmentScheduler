package com.evanwahrmund.appointmentscheduler;

import com.evanwahrmund.appointmentscheduler.daos.CustomerDatabaseDao;
import javafx.collections.ObservableList;

import java.sql.SQLException;

public class Customers {

    private static ObservableList<Customer> customers;
    static{
        customers = CustomerDatabaseDao.getInstance().getAllCustomers();
    }

    public static ObservableList<Customer> getCustomers(){
        return customers;
    }
    public static Customer getCustomer(int id){
        for(Customer customer: customers) {
            if (customer.getId() == id)
                return customer;
        }
        return null;
    }
    public static void createCustomer(Customer customer) throws SQLException {
        CustomerDatabaseDao.getInstance().createCustomer(customer);
        customers.add(customer);
    }
    public static void updateCustomer(Customer customer) throws SQLException {
        CustomerDatabaseDao.getInstance().updateCustomer(customer);
        customers.set(customers.indexOf(customer), customer);


    }
    public static void deleteCustomer(Customer customer) throws SQLException{
        CustomerDatabaseDao.getInstance().deleteCustomer(customer);
        customers.remove(customer);

    }
}
