package com.evanwahrmund.appointmentscheduler.interfaces;

import com.evanwahrmund.appointmentscheduler.Customer;
import javafx.collections.ObservableList;

import java.sql.SQLException;

public interface CustomerDao {
    ObservableList<Customer> getAllCustomers();
    Customer getCustomer(int id);
    void createCustomer(Customer customer) throws SQLException;
    boolean updateCustomer(Customer customer);
    void deleteCustomer(Customer customer) throws SQLException;

}
