package com.evanwahrmund.appointmentscheduler.interfaces;

import com.evanwahrmund.appointmentscheduler.models.Contact;
import javafx.collections.ObservableList;

/**
 * Interface representing Contact operations
 */
public interface ContactDao {
    /**
     * Gets list of all Contacts
     * @return ObservableList of all Contacts
     */
    ObservableList<Contact> getAllContacts();

    /**
     * Gets Contact with given id
     * @param id int id to search for
     * @return Contact with given id, null otherwise
     */
    Contact getContact(int id);
}
