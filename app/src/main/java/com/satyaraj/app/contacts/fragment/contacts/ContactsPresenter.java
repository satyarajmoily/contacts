package com.satyaraj.app.contacts.fragment.contacts;

import com.satyaraj.app.contacts.base.BasePresenter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class ContactsPresenter extends BasePresenter implements ContactContract.Action {

    private ContactContract.View mView;
    private ContactsRepository mContactsRepository;

    @Inject
    public ContactsPresenter(ContactsFragment view, ContactsRepository contactsRepository) {
        this.mView = view;
        this.mContactsRepository = contactsRepository;
        this.mContactsRepository.onAttach(this);
    }

    @Override
    public void getContacts(){
        mContactsRepository.getContactsFromDatabase();
    }

    void onContactFetched(List<Contact> result) {
        mView.displayContacts(result);
    }

    @Override
    public void getNewContacts(List<Contact> contactList, int count){
        List<Contact> contacts = new ArrayList<>(0);
        for (int i = 0; i < count; i++){
            contacts.add(contactList.get(contactList.size() - 1 - i));
        }
        mView.displayNewContacts(contacts);
    }
}
