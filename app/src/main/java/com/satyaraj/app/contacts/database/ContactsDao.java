package com.satyaraj.app.contacts.database;

import com.satyaraj.app.contacts.fragment.contacts.Contact;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface ContactsDao {
    @Query("SELECT * FROM contact")
    List<Contact> getAll();

    @Insert
    void insertAll(List<Contact> contactList);

    @Insert
    void insertContact(Contact contact);

//    @Delete
//    void delete(Contact user);
}