package com.satyaraj.app.contacts.application;

import android.content.Context;

import com.satyaraj.app.contacts.database.AppDatabase;

import dagger.Module;
import dagger.Provides;

@Module
public class ContextModule {

    private final Context context;
    private final ContactApplication application;

    public ContextModule(ContactApplication context) {
        this.application = context;
        this.context = context.getApplicationContext();
    }

    @Provides
    public ContactApplication getApplication() {
        return application;
    }

    @Provides
    @ApplicationContext
    public Context context() {
        return context;
    }

    @Provides
    public AppDatabase getAppDatabase(){
        return AppDatabase.getAppDatabase(context);
    }
}