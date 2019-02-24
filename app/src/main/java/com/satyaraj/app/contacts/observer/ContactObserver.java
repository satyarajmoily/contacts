package com.satyaraj.app.contacts.observer;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.provider.ContactsContract;

import com.satyaraj.app.contacts.application.ContactApplication;
import com.satyaraj.app.contacts.database.AppDatabase;
import com.satyaraj.app.contacts.fragment.contacts.Contact;
import com.satyaraj.app.contacts.prefs.SharedPrefsHelper;
import com.satyaraj.app.contacts.prefs.SharedPrefsKey;

public class ContactObserver extends ContentObserver {

    private Context context;
    private SharedPrefsHelper mSharedPrefsHelper;
    private AppDatabase mAppDatabase;

    public ContactObserver(Handler handler, Context context) {
        super(handler);
        this.context = context;
        this.mSharedPrefsHelper = ContactApplication.getSharedPrefsHelper();
        this.mAppDatabase = ContactApplication.getAppDatabase();
    }

    @Override
    public void onChange(boolean selfChange, Uri uri) {
        super.onChange(selfChange, uri);
        if (!selfChange) {
            Contact contacts = new Contact();
            int previousContactsCount = mSharedPrefsHelper.get(SharedPrefsKey.PREF_KEY_CONTACTS_COUNT,0);
            int contactsAddedCount = mSharedPrefsHelper.get(SharedPrefsKey.PREF_KEY_CONTACTS_ADDED,0);
            ContentResolver cr = context.getContentResolver();
            Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI,
                    null, null, null, null);
            if (cursor != null &&
                    cursor.getCount() > 0
                    && cursor.getCount() > previousContactsCount) {
                //moving cursor to last position
                //to get last element added
                cursor.moveToLast();
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));

                String name = cursor.getString(cursor.getColumnIndex(
                        ContactsContract.Contacts.DISPLAY_NAME));

                String picUrl = cursor.getString(cursor.getColumnIndex(
                        ContactsContract.Contacts.PHOTO_THUMBNAIL_URI));

                contacts.setName(name);
                contacts.setPicUrl(picUrl);

                int hasPhoneNumber = cursor.getInt(cursor.getColumnIndex(
                        ContactsContract.Contacts.HAS_PHONE_NUMBER));

                contacts.setHasPhoneNumber(hasPhoneNumber);

                if (hasPhoneNumber > 0) {
                    Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                    new String[]{id},
                            null);

                    if (pCur != null) {
                        if (pCur.moveToNext()) {
                            String contactNumber = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            contacts.setPhoneNumber(contactNumber);
                        }
                        pCur.close();
                    }
                }else {
                    contacts.setHasPhoneNumber(0);
                }
                mSharedPrefsHelper.put(SharedPrefsKey.PREF_KEY_CONTACTS_COUNT, ++previousContactsCount);
                mSharedPrefsHelper.put(SharedPrefsKey.PREF_KEY_CONTACTS_ADDED, ++ contactsAddedCount);
                mAppDatabase.contactDao().insertContact(contacts);
                cursor.close();
            }
        }
    }
}
