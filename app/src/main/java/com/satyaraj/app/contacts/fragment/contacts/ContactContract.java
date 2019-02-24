package com.satyaraj.app.contacts.fragment.contacts;

import java.util.List;

public interface ContactContract {

    interface View {
        void displayContacts(List<Contact> contactList);

        void displayNewContacts(List<Contact> contacts);
    }

    interface Action {
        void getContacts();

        void getNewContacts(List<Contact> contactList, int count);
    }
}
