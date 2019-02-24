package com.satyaraj.app.contacts.fragment.contacts;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.ContactsContract;

import com.satyaraj.app.contacts.application.ContactApplication;
import com.satyaraj.app.contacts.base.BaseRepository;
import com.satyaraj.app.contacts.database.AppDatabase;
import com.satyaraj.app.contacts.prefs.SharedPrefsHelper;
import com.satyaraj.app.contacts.prefs.SharedPrefsKey;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ContactsRepository extends BaseRepository<ContactsPresenter> {

    private Disposable mCompositeDisposable;
    private ContentResolver mContentResolver;
    private SharedPrefsHelper mSharedPrefsHelper;
    private AppDatabase mAppDatabase;

    @Inject
    public ContactsRepository(ContentResolver contentResolver) {
        this.mCompositeDisposable = new CompositeDisposable();
        this.mContentResolver = contentResolver;
        this.mSharedPrefsHelper = ContactApplication.getSharedPrefsHelper();
        this.mAppDatabase = ContactApplication.getAppDatabase();
    }

    public void getContactsFromDatabase(){
        mCompositeDisposable = Observable.fromCallable(() -> mAppDatabase.contactDao().getAll())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(contactList -> {
                    if (contactList.isEmpty()){
                        getContactsFromPhone();
                    }else {
                        getActions().onContactFetched(contactList);
                    }
                });

    }

    private void getContactsFromPhone() {
        mCompositeDisposable = Observable.fromCallable(this::getContactListFromPhone)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(contactList -> {
                    getActions().onContactFetched(contactList);
                    storeIntoDatabase(contactList);
                });
    }

    private List<Contact> getContactListFromPhone() {

        List<Contact> contactsList = new ArrayList<>();

        Cursor cur = mContentResolver.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, ContactsContract.Contacts.DISPLAY_NAME + " asc");

        if ((cur != null ? cur.getCount() : 0) > 0) {
            mSharedPrefsHelper.put(SharedPrefsKey.PREF_KEY_CONTACTS_COUNT, cur.getCount());
            while (cur.moveToNext()) {
                Contact contacts = new Contact();

                String id = cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts._ID));

                String name = cur.getString(cur.getColumnIndex(
                        ContactsContract.Contacts.DISPLAY_NAME));

                String picUrl = cur.getString(cur.getColumnIndex(
                        ContactsContract.Contacts.PHOTO_THUMBNAIL_URI));

                contacts.setName(name);
                contacts.setPicUrl(picUrl);

                int hasPhoneNumber = cur.getInt(cur.getColumnIndex(
                        ContactsContract.Contacts.HAS_PHONE_NUMBER));

                if (hasPhoneNumber > 0) {
                    contacts.setHasPhoneNumber(1);
                    Cursor pCur = mContentResolver.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    if (pCur != null) {
                        if (pCur.moveToNext()) {
                            String phoneNo = pCur.getString(pCur.getColumnIndex(
                                    ContactsContract.CommonDataKinds.Phone.NUMBER));

                            contacts.setPhoneNumber(phoneNo);
                        }
                    }
                    if (pCur != null) {
                        pCur.close();
                    }
                } else {
                    contacts.setHasPhoneNumber(0);
                }

                contactsList.add(contacts);
            }
        }
        if (cur != null) {
            cur.close();
        }

        return contactsList;
    }

    private void storeIntoDatabase(List<Contact> contactList){

        mCompositeDisposable = Observable.fromCallable(() -> {
             mAppDatabase.contactDao().insertAll(contactList);
             return true;
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

}
