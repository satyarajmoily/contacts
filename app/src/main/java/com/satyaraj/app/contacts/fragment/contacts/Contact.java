package com.satyaraj.app.contacts.fragment.contacts;

import java.io.Serializable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity ( tableName = "contact")
public class Contact implements Serializable {

    @PrimaryKey (autoGenerate = true)
    public int id;
    @ColumnInfo(name = "contact_name")
    public String name;
    @ColumnInfo(name = "contact_picture")
    public String picUrl;
    @ColumnInfo(name = "has_phone_number")
    public int hasPhoneNumber;
    @ColumnInfo(name = "phone_number")
    public String phoneNumber;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public int getHasPhoneNumber() {
        return hasPhoneNumber;
    }

    public void setHasPhoneNumber(int hasPhoneNumber) {
        this.hasPhoneNumber = hasPhoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
