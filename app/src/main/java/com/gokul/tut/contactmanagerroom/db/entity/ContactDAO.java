package com.gokul.tut.contactmanagerroom.db.entity;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao

public interface ContactDAO {
    @Insert
    public long addContact(Contact contact);

    @Update
    public void updateContact(Contact contact);

    @Delete
    public void deleteContact(Contact contact);

    @Query("select * from contacts")
    public List<Contact> getContact();

    @Query("select * from contacts where contact_id==:contactId")
    public Contact getContact(long contactId);


}
