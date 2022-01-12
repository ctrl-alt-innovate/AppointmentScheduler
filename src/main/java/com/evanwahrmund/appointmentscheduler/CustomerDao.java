package com.evanwahrmund.appointmentscheduler;

import javafx.collections.ObservableList;

import java.sql.SQLException;

public interface CustomerDao {
    ObservableList<Customer> getAllCustomers();
    Customer getCustomer(int id);
    boolean createCustomer(Customer customer) throws SQLException;
    boolean updateCustomer(Customer customer);
    boolean deleteCustomer(Customer customer);

}
