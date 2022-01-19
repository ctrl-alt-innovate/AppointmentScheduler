package com.evanwahrmund.appointmentscheduler.models;

import com.evanwahrmund.appointmentscheduler.daos.CustomerDatabaseDao;
import javafx.collections.ObservableList;

import java.sql.SQLException;

/**
 * Represents all customers in the database.
 * Used to set values of customer tables and observe changes.
 * Ensures consistent display of data between database and application.
 * Initializes list of customers when first called by Application
 */
public class Customers {
    /**
     * ObservableList representing all customers in the database.
     * Used to set values of Customer TableViews that can be observed
     */
    private static ObservableList<Customer> customers;
    static{
        customers = CustomerDatabaseDao.getInstance().getAllCustomers();
    }

    /**
     * Gets all customers
     * @return ObservableList of all Customers
     */
    public static ObservableList<Customer> getCustomers(){
        return customers;
    }

    /**
     * Selects Customer with given id.
     * Ensures any customer selected is associated with its database representation
     * @param id int to search for
     * @return Customer with given id, null otherwise
     */
    public static Customer getCustomer(int id){
        for(Customer customer: customers) {
            if (customer.getId() == id)
                return customer;
        }
        return null;
    }

    /**
     * Adds new Customer to database and list of Customers
     * @param customer Customer to be added
     * @throws SQLException if any errors occur while adding to the database
     */
    public static void createCustomer(Customer customer) throws SQLException {
        CustomerDatabaseDao.getInstance().createCustomer(customer);
        customers.add(customer);
    }

    /**
     * Updates given Customer in database and in list of customers
     * @param customer Customer to be updated
     * @throws SQLException if any errors occur while updating in database
     */
    public static void updateCustomer(Customer customer) throws SQLException {
        CustomerDatabaseDao.getInstance().updateCustomer(customer);
        customers.set(customers.indexOf(customer), customer);


    }

    /**
     * Deletes given Customer from database and list of Customers
     * @param customer Customer to be deleted
     * @throws SQLException if any errors occur while deleting from database
     */
    public static void deleteCustomer(Customer customer) throws SQLException{
        CustomerDatabaseDao.getInstance().deleteCustomer(customer);
        customers.remove(customer);

    }
}
