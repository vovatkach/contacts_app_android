package com.example.mukola.contactapplication.model.repositories;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.mukola.contactapplication.model.database.DatabaseContract;
import com.example.mukola.contactapplication.model.database.DatabaseHelper;
import com.example.mukola.contactapplication.model.models.Contact;

public class UpdateContactRepositoryImpl implements UpdateContactRepository {

    @NonNull
    private Context context;

    public UpdateContactRepositoryImpl(@NonNull Context context) {
        this.context = context;
    }

    @Override
    public void updateContact(@NonNull int userId, @NonNull Contact contact, @NonNull updateContactCallback callback) {
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        SQLiteDatabase database = dbHelper.getWritableDatabase();


        long rowID = 0;
        ContentValues cv = new ContentValues();
        Log.d("LOG_TAG", "--- Updates in contact: ---");

        cv.put(DatabaseContract.CONTACTS.COLUMN_NAME,contact.getName());
        cv.put(DatabaseContract.CONTACTS.COLUMN_NUMBER,contact.getNumber());
        cv.put(DatabaseContract.CONTACTS.COLUMN_EMAIL,contact.getEmail());
        cv.put(DatabaseContract.CONTACTS.COLUMN_ADDRESS,contact.getAddress());
        cv.put(DatabaseContract.CONTACTS.COLUMN_COMPANY,contact.getCompany());
        cv.put(DatabaseContract.CONTACTS.COLUMN_PHOTO_URL,contact.getPhotoUrl());
        if (contact.isFavorite()){
            cv.put(DatabaseContract.CONTACTS.COLUMN_IS_FAVORITE, "true");
        }else {
            cv.put(DatabaseContract.CONTACTS.COLUMN_IS_FAVORITE,"false");

        }
        rowID =    database.update(DatabaseContract.CONTACTS.TABLE, cv, "id" + "=" + contact.getId(), null);
        Log.d("LOG_TAG", "row updated, ID = " + rowID);

        database.close();
        dbHelper.close();
    }
}
