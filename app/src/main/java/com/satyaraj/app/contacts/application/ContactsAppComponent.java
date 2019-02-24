package com.satyaraj.app.contacts.application;

import com.satyaraj.app.contacts.database.AppDatabase;
import com.satyaraj.app.contacts.prefs.SharedPrefsHelper;
import com.satyaraj.app.contacts.prefs.SharedPrefsModule;

import dagger.Component;

@ContactAppScope
@Component(modules = {SharedPrefsModule.class})
public interface ContactsAppComponent {
    AppDatabase getAppDatabase();

    SharedPrefsHelper getSharedPrefsHelper();
}
