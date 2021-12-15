package com.evanwahrmund.appointmentscheduler;

import javafx.collections.ObservableList;

public interface ContactDao {
    ObservableList<Contact> getAllContacts();
    Contact getContact(int id);
    boolean createContact(Contact contact);
    boolean updateContact(Contact contact);
    boolean deleteContact(Contact contact);
}
