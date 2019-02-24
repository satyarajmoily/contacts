package com.satyaraj.app.contacts.fragment.contacts.di;

import android.content.ContentResolver;

import com.satyaraj.app.contacts.fragment.contacts.ContactsFragment;

import java.util.Objects;

import dagger.Module;
import dagger.Provides;

@Module
public class ContactListModule {

    private ContactsFragment mContactsFragment;

    public ContactListModule(ContactsFragment contactsFragment) {
        this.mContactsFragment = contactsFragment;
    }

    @Provides
    ContactsFragment getmContactsFragment(){
        return mContactsFragment;
    }

    @Provides
    ContentResolver getContentResolver(){
        return  Objects.requireNonNull(mContactsFragment.getActivity()).getContentResolver();
    }
}
