package com.satyaraj.app.contacts.fragment.contacts.di;

import com.satyaraj.app.contacts.fragment.contacts.ContactsFragment;

import dagger.Component;

@Component(modules = ContactListModule.class)
public interface ContactListComponent {

    void  injectContactFragment(ContactsFragment contactsFragment);
}
