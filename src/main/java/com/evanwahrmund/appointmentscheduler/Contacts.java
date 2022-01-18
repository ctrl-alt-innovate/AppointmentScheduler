package com.evanwahrmund.appointmentscheduler;

import com.evanwahrmund.appointmentscheduler.daos.ContactDatabaseDao;
import javafx.collections.ObservableList;

public class Contacts {

    private static ObservableList<Contact> contacts;
    //private static Contacts INSTANCE;
    static {
        contacts = ContactDatabaseDao.getInstance().getAllContacts();
    }

    public static ObservableList<Contact> getContacts(){
        return contacts;
    }
    public static Contact getContact(int id){
        for(Contact contact: contacts){
            if(contact.getId() == id){
                return contact;
            }
        }
        System.out.println("Contact with ID: " + id + " not found.");
        return null;
    }
    public static void createContact(Contact contact){
        if(ContactDatabaseDao.getInstance().createContact(contact)){
            contacts.add(contact);
        }
    }
    public static void updateContact(Contact contact){
        if(ContactDatabaseDao.getInstance().updateContact(contact)){
            contacts.set(contacts.indexOf(contact), contact);
        }
    }
    public static void deleteContact(Contact contact){
        if(ContactDatabaseDao.getInstance().deleteContact(contact)){
            contacts.remove(contact);
        }
    }

}
