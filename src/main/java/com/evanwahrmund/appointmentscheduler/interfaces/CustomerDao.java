package com.evanwahrmund.appointmentscheduler.interfaces;

import com.evanwahrmund.appointmentscheduler.Customer;
import javafx.collections.ObservableList;

import java.sql.SQLException;

/**
 * Interface representing Customer operations
 */
public interface CustomerDao {

    /**
     * Gets list of all Customers
     * @return ObservableList of all Customers
     */
    ObservableList<Customer> getAllCustomers();

    /**
     * Gets Customer with given id
     * @param id int id to search for
     * @return Customer with given id, null otherwise
     */
    Customer getCustomer(int id);

    /**
     * Create new customer
     * @param customer Customer to be created
     * @throws Exception if any errors occur while adding Customer
     */
    void createCustomer(Customer customer) throws Exception;

    /**
     * Updates given Customer
     * @param customer Customer to be updated
     * @throws Exception if any errors occur while update Customer
     */
    void updateCustomer(Customer customer)throws Exception;

    /**
     * Deletes given Customer
     * @param customer customer to be deleted
     * @throws Exception if any errors occur while deleting Customer
     */
    void deleteCustomer(Customer customer) throws Exception;

}
