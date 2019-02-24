package com.satyaraj.app.contacts.prefs;

import android.content.Context;
import android.content.SharedPreferences;

import com.satyaraj.app.contacts.application.ApplicationContext;
import com.satyaraj.app.contacts.application.ContactAppScope;
import com.satyaraj.app.contacts.application.ContextModule;

import dagger.Module;
import dagger.Provides;

@Module(includes = ContextModule.class)
public class SharedPrefsModule {

    @Provides
    @ContactAppScope
    public SharedPrefsHelper sharedPrefsHelper(@ApplicationContext Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        return new SharedPrefsHelper(sharedPreferences);
    }

}
