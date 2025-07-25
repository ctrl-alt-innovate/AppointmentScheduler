package com.evanwahrmund.appointmentscheduler.daos;

import java.sql.SQLException;

import com.evanwahrmund.appointmentscheduler.models.User;
import com.evanwahrmund.appointmentscheduler.interfaces.UserDao;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Database impolementation of UserDao
 */
public class UserDatabaseDao implements UserDao {
    /**
     * Singleton instance
     */
    private static UserDatabaseDao INSTANCE;

    /**
     * Gets Instance of UserDatabaseDao
     * @return singleton instance of UserDatabaseDao
     */
    public static UserDatabaseDao getInstance(){
        if (INSTANCE == null)
            INSTANCE = new UserDatabaseDao();
        return INSTANCE;
    }

    /**
     * Selects all Users from database
     * @return ObservableList of all Users in database
     */
    public ObservableList<User> getAllUsers(){
        ObservableList<User> users = FXCollections.observableArrayList();
        String sql = "SELECT User_ID, User_Name, Password FROM users;";
        try(var rs = DatabaseConnection.getConnection().prepareStatement(sql).executeQuery()){
            while(rs.next()){
                User user = new User(rs.getInt("User_ID"), rs.getString("User_Name"),
                                        rs.getString("Password"));
                users.add(user);
            }
        } catch(SQLException ex){
            System.out.println("Error in retrieving all users.");
        }
        return users;
    }

    /**
     * Gets User with given id
     * @param id int to search for
     * @return User with given id, null otherwise
     */
    public User getUser(int id){
        String sql = "SELECT User_ID, User_Name, Password FROM users WHERE User_ID = ?";
        try(var ps = DatabaseConnection.getConnection().prepareStatement(sql)){
            ps.setInt(1, id);
            try(var rs = ps.executeQuery()){
                if(rs.next()){
                    User user = new User(rs.getInt("User_ID"), rs.getString("User_Name"),
                            rs.getString("Password"));
                    return user;
                }
            }
        }catch(SQLException ex){
            ex.printStackTrace();
        }
        return null;
    }
    /*
    public boolean createUser(User user){
        String sql = "INSERT INTO users(User_Name, Password, Created_By, Last_Updated_By) VALUES(?, ?, ?, ?);";
        try (var ps = DatabaseConnection.getConnection().prepareStatement(sql)){
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, "Application");
            ps.setString(4, "Application");
            ps.executeUpdate();
            try( var rs = ps.getGeneratedKeys()){
                if(rs.next()){
                    user.setUserId(rs.getInt(1));
                    return true;
                }
                System.out.println("Error: User id not set.");
                return false;
            }
        } catch(SQLException ex){
            System.out.println("Error: User not added.");
            return false;
        }
    }
    public boolean updateUser(User user){
        String sql = "UPDATE users SET User_Name = ?, Password = ? WHERE User_ID = ?;";
        try(var ps = DatabaseConnection.getConnection().prepareStatement(sql)){
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setInt(3, user.getUserId());
            ps.executeUpdate();
            return true;
        }catch(SQLException ex){
            System.out.println("Error: User with ID - " + user.getUserId() + " not updated.");
            return false;
        }
    }
    public boolean deleteUser(User user){
        String sql = "DELETE FROM users WHERE User_ID = ?;";
        try(var ps = DatabaseConnection.getConnection().prepareStatement(sql)){
            ps.setInt(1, user.getUserId());
            ps.executeUpdate();
            return true;
        } catch(SQLException ex){
            System.out.println("Error: User with ID - " + user.getUserId() + " not deleted.");
            return false;
        }
    }
*/


}
