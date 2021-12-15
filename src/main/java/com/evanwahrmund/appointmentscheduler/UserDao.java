package com.evanwahrmund.appointmentscheduler;

import javafx.collections.ObservableList;

public interface UserDao {
    ObservableList<User> getAllUsers();
    User getUser(int userId);
    boolean createUser(User user);
    boolean updateUser(User user);
    boolean deleteUser(User user);
}
