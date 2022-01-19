package com.evanwahrmund.appointmentscheduler.interfaces;

import com.evanwahrmund.appointmentscheduler.models.User;
import javafx.collections.ObservableList;

/**
 * Interface representing User operations
 */
public interface UserDao {
    /**
     * Gets list of all Users
     * @return ObservableList of all Users
     */
    ObservableList<User> getAllUsers();

    /**
     * Gets User with given id
     * @param userId int id to search for
     * @return User with given id, null otherwise
     */
    User getUser(int userId);
}
