package com.evanwahrmund.appointmentscheduler.models;

import com.evanwahrmund.appointmentscheduler.daos.UserDatabaseDao;
import javafx.collections.ObservableList;

/**
 * Represents all Users in the database.
 * Initializes list of Users when first called in application
 */
public class Users {

    /**
     * ObservableList representing all users in the database
     */
    private static ObservableList<User> users;
    static{
        users = UserDatabaseDao.getInstance().getAllUsers();
    }

    /**
     * Gets list of all Users
     * @return ObservableList of all Users
     */
    public static ObservableList<User> getUsers(){
        return users;
    }

    /**
     * Selects User with given id
     * @param id int id of user to search for
     * @return User with given id, null otherwise
     */
    public static User getUser(int id){
        for(User user: users){
            if(user.getUserId() == id)
                return user;
        }
        return null;
    }



}
