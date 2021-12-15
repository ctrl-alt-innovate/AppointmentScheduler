package com.evanwahrmund.appointmentscheduler;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Users {

    private static ObservableList<User> users;
    static{
        users = UserDatabaseDao.getInstance().getAllUsers();
    }

    public static ObservableList<User> getUsers(){
        return users;
    }

    public static User getUser(int id){
        for(User user: users){
            if(user.getUserId() == id)
                return user;
        }
        return null;
    }

    public static void createUser(User user){
        if(UserDatabaseDao.getInstance().createUser(user)){
            users.add(user);
        }
    }

    public static void updateUser(User user){
        if(UserDatabaseDao.getInstance().updateUser(user)){
            users.set(users.indexOf(user), user);
        }
    }

    public static void deleteUser(User user){
        if(UserDatabaseDao.getInstance().deleteUser(user)){
            users.remove(user);
        }
    }

}
