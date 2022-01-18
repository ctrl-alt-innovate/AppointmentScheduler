package com.evanwahrmund.appointmentscheduler;

/**
 * Model Object for User table of database.
 */
public class User {
    /**
     * int representing User_ID coulumn in database
     */
    private int userId;
    /**
     * String representing User_Name column in databse
     */
    private String username;
    /**
     * String representing Password coulumn in database
     */
    private String password;

    /**
     * Creates a User from database data
     * @param userId int of User_ID column
     * @param username String of User_Name column
     * @param password String of Password column
     */
    public User(int userId, String username, String password){
        this.userId = userId;
        this.username = username;
        this.password = password;
    }

    /**
     * Gets Id of User
     * @return int representing User_ID
     */
    public int getUserId(){
        return userId;
    }

    /**
     * Updates Id of User
     * @param userId int to replace userId
     */
    public void setUserId(int userId){
        this.userId = userId;
    }

    /**
     * Gets username of User
     * @return String representing User_Name
     */
    public String getUsername(){
        return username;
    }

    /**
     * Updates username of User
     * @param username String to replace username
     */
    public void setUsername(String username){
        this.username = username;
    }

    /**
     * Gets password of User
     * @return String representing Password
     */
    public String getPassword(){
        return password;
    }

    /**
     * Updates password of User
     * @param password String to replace password
     */
    public void setPassword(String password){
        this.password = password;
    }

}
