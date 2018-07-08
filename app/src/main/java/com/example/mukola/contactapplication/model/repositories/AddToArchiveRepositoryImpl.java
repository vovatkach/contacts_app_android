package com.example.mukola.contactapplication.model.repositories;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.mukola.contactapplication.model.database.DatabaseContract;
import com.example.mukola.contactapplication.model.database.DatabaseHelper;
import com.example.mukola.contactapplication.model.models.Contact;

public class AddToArchiveRepositoryImpl implements AddToArchiveRepository {

    @NonNull
    private Context context;

    public AddToArchiveRepositoryImpl(@NonNull Context context) {
        this.context = context;
    }


    @Override
    public void addToArchive(@NonNull int userId, @NonNull Contact contact, @NonNull addToArchiveCallback callback) {
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        long rowID = 0;
        ContentValues cv = new ContentValues();
        Log.d("LOG_TAG", "--- Insert in ARCHIVE: ---");
        // подготовим данные для вставки в виде пар: наименование столбца -
        // значение
        Log.d("LOG_TAG", "userId = " + userId);
        cv.put(DatabaseContract.ARCHIVED.COLUMN_USER_ID, userId);

        Log.d("LOG_TAG", "name = " + contact.getName());
        cv.put(DatabaseContract.ARCHIVED.COLUMN_NAME, contact.getName());

        Log.d("LOG_TAG", "number = " + contact.getNumber());
        cv.put(DatabaseContract.ARCHIVED.COLUMN_NUMBER, contact.getNumber());

        Log.d("LOG_TAG", "email = " + contact.getEmail());
        cv.put(DatabaseContract.ARCHIVED.COLUMN_EMAIL, contact.getEmail());

        Log.d("LOG_TAG", "address = " + contact.getAddress());
        cv.put(DatabaseContract.ARCHIVED.COLUMN_ADDRESS, contact.getAddress());

        Log.d("LOG_TAG", "company = " + contact.getCompany());
        cv.put(DatabaseContract.ARCHIVED.COLUMN_COMPANY, contact.getCompany());

        Log.d("LOG_TAG", "photoUrl = " + contact.getPhotoUrl());
        cv.put(DatabaseContract.ARCHIVED.COLUMN_PHOTO_URL, contact.getPhotoUrl());

        String s = "false";

        if (contact.isFavorite()){
            s = "true";
        }

        Log.d("LOG_TAG", "isFavorite = " + s);
        cv.put(DatabaseContract.ARCHIVED.COLUMN_IS_FAVORITE, s);

        Log.d("LOG_TAG", "blacklistId = " + contact.getBlacklistId());
        cv.put(DatabaseContract.ARCHIVED.COLUMN_BLACKLIST_ID, contact.getBlacklistId());

        rowID = database.insert(DatabaseContract.ARCHIVED.TABLE, null, cv);

        if (rowID == -1){
            callback.notSuccessfull();
        }else {
            Log.d("LOG_TAG", "row inserted, ID = " + rowID);
            callback.addedSuccessfull();
        }
    }
}
