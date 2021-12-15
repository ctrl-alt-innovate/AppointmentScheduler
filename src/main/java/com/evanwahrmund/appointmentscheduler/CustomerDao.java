package com.evanwahrmund.appointmentscheduler;

import javafx.collections.ObservableList;

public interface CustomerDao {
    ObservableList<Customer> getAllCustomers();
    Customer getCustomer(int id);
    boolean createCustomer(Customer customer);
    boolean updateCustomer(Customer customer);
    boolean deleteCustomer(Customer customer);

}
