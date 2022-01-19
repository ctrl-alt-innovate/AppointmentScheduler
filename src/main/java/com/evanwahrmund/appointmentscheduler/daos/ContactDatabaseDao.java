package com.evanwahrmund.appointmentscheduler.daos;

import java.sql.SQLException;
import java.sql.Statement;

import com.evanwahrmund.appointmentscheduler.models.Contact;
import com.evanwahrmund.appointmentscheduler.interfaces.ContactDao;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ContactDatabaseDao implements ContactDao {

    private static ContactDatabaseDao INSTANCE;

    public static ContactDatabaseDao getInstance(){
        if (INSTANCE == null)
            INSTANCE = new ContactDatabaseDao();
        return INSTANCE;
    }

    public ObservableList<Contact> getAllContacts() {
        ObservableList<Contact> contacts = FXCollections.observableArrayList();
        String sql = "SELECT Contact_ID, Contact_Name, Email FROM contacts;";
        try(var rs = DatabaseConnection.getConnection().prepareStatement(sql).executeQuery()){
            while(rs.next()){
                Contact contact = new Contact(rs.getInt("Contact_ID"), rs.getString("Contact_Name"),
                                               rs.getString("Email"));
                contacts.add(contact);
            }
        } catch(SQLException ex) {
            System.out.println("Error in retrieving all contacts.");
        }
        return contacts;
    }

    //HAVE NOT CHECKED
    public Contact getContact(int id){
        String sql = "SELECT Contact_ID, Contact_Name, Email FROM contacts WHERE Contact_ID = ?;";
        try(var ps = DatabaseConnection.getConnection().prepareStatement(sql)){
            ps.setInt(1, id);
            try(var rs = ps.executeQuery()){
                if(rs.next()){
                    Contact contact = new Contact(rs.getInt("Contact_ID"), rs.getString("Contact_Name"),
                            rs.getString("Email"));
                    return contact;
                }
            }
        } catch (SQLException ex){
            ex.printStackTrace();
        }
        return null;
    }
    public boolean createContact(Contact contact){
        String sql = "INSERT INTO contacts(Contact_Name, Email) VALUES(?, ?);";
        try(var ps = DatabaseConnection.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            ps.setString(1, contact.getName());
            ps.setString(2, contact.getEmail());
            ps.executeUpdate();
            try(var rs = ps.getGeneratedKeys()){
                if (rs.next()){
                    contact.setId(rs.getInt(1));
                    return true;
                }
                System.out.println("Error: Customer id not set.");
                return false;
            }
        } catch(SQLException ex){
            System.out.println("Error: Contact not created.");
            return false;
        }
    }
    public boolean updateContact(Contact contact){
        String sql = "UPDATE contacts SET Contact_Name = ?, Email = ? WHERE Contact_ID = ?;";
        try(var ps = DatabaseConnection.getConnection().prepareStatement(sql)){
            ps.setString(1, contact.getName());
            ps.setString(2, contact.getEmail());
            ps.setInt(3, contact.getId());
            ps.executeUpdate();
            return true;
        } catch(SQLException ex){
            System.out.println("Error:Contact with ID - " + contact.getId() + " not updated.");
            return false;
        }
    }
    public boolean deleteContact(Contact contact){
        String sql = "DELETE FROM contacts WHERE Contact_ID = ?;";
        try(var ps = DatabaseConnection.getConnection().prepareStatement(sql)){
            ps.setInt(1, contact.getId());
            ps.executeUpdate();
            return true;
        } catch(SQLException ex){
            System.out.println("Error: Contact with ID - " + contact.getId() + " not deleted.");
            return false;
        }
    }
}
