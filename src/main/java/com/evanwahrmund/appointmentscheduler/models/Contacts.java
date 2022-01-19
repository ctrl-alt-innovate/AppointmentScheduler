package com.evanwahrmund.appointmentscheduler.models;

import com.evanwahrmund.appointmentscheduler.daos.ContactDatabaseDao;
import com.evanwahrmund.appointmentscheduler.models.Contact;
import javafx.collections.ObservableList;

/**
 * Represents all Contacts in the database.
 * Initializes list of Contacts when first called by application
 */
public class Contacts {
    /**
     * ObservableList representing all Contacts in database
     */
    private static ObservableList<Contact> contacts;

    static {
        contacts = ContactDatabaseDao.getInstance().getAllContacts();
    }

    /**
     * Get all Contacts
     * @return ObservableList of all Contacts
     */
    public static ObservableList<Contact> getContacts(){
        return contacts;
    }

    /**
     * Select Contact with given id
     * @param id id to search for
     * @return Contact with given id, null otherwise
     */
    public static Contact getContact(int id){
        for(Contact contact: contacts){
            if(contact.getId() == id){
                return contact;
            }
        }

        return null;
    }

}
