package com.satyaraj.app.contacts.application;

import android.app.Application;

import com.satyaraj.app.contacts.database.AppDatabase;
import com.satyaraj.app.contacts.prefs.SharedPrefsHelper;

public class ContactApplication extends Application {

    private static SharedPrefsHelper sharedPrefsHelper;
    ContactsAppComponent contactsAppComponent;
    private static AppDatabase appDatabase;

    @Override
    public void onCreate() {
        super.onCreate();
        initializeComponents();
    }

    private void initializeComponents() {
        contactsAppComponent =
                DaggerContactsAppComponent
                        .builder()
                        .contextModule(new ContextModule(this))
                        .build();
        sharedPrefsHelper = contactsAppComponent.getSharedPrefsHelper();
        appDatabase = contactsAppComponent.getAppDatabase();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    public static SharedPrefsHelper getSharedPrefsHelper() {
        return sharedPrefsHelper;
    }

    public static AppDatabase getAppDatabase() {
        return appDatabase;
    }
}
