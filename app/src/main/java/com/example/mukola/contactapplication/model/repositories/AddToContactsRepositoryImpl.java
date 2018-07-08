package com.example.mukola.contactapplication.model.repositories;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.mukola.contactapplication.model.database.DatabaseContract;
import com.example.mukola.contactapplication.model.database.DatabaseHelper;
import com.example.mukola.contactapplication.model.models.Contact;

import java.util.ArrayList;

public class AddToContactsRepositoryImpl implements AddToContactsRepository{

    @NonNull
    private Context context;

    public AddToContactsRepositoryImpl(@NonNull Context context) {
        this.context = context;
    }

    @Override
    public void addToContacts(@NonNull int userId, @NonNull Contact contact, @NonNull addToContactsCallback callback) {
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        long rowID = 0;
        ContentValues cv = new ContentValues();
        Log.d("LOG_TAG", "--- Insert in CONTACTS: ---");
        // подготовим данные для вставки в виде пар: наименование столбца -
        // значение
        Log.d("LOG_TAG", "userId = " + userId);
        cv.put(DatabaseContract.CONTACTS.COLUMN_USER_ID, userId);

        Log.d("LOG_TAG", "name = " + contact.getName());
        cv.put(DatabaseContract.CONTACTS.COLUMN_NAME, contact.getName());

        Log.d("LOG_TAG", "number = " + contact.getNumber());
        cv.put(DatabaseContract.CONTACTS.COLUMN_NUMBER, contact.getNumber());

        Log.d("LOG_TAG", "email = " + contact.getEmail());
        cv.put(DatabaseContract.CONTACTS.COLUMN_EMAIL, contact.getEmail());

        Log.d("LOG_TAG", "address = " + contact.getAddress());
        cv.put(DatabaseContract.CONTACTS.COLUMN_ADDRESS, contact.getAddress());

        Log.d("LOG_TAG", "company = " + contact.getCompany());
        cv.put(DatabaseContract.CONTACTS.COLUMN_COMPANY, contact.getCompany());

        Log.d("LOG_TAG", "photoUrl = " + contact.getPhotoUrl());
        cv.put(DatabaseContract.CONTACTS.COLUMN_PHOTO_URL, contact.getPhotoUrl());

        String s = "false";

        if (contact.isFavorite()){
            s = "true";
        }

        Log.d("LOG_TAG", "isFavorite = " + s);
        cv.put(DatabaseContract.CONTACTS.COLUMN_IS_FAVORITE, s);

        Log.d("LOG_TAG", "blacklistId = " + contact.getBlacklistId());
        cv.put(DatabaseContract.CONTACTS.COLUMN_BLACKLIST_ID, contact.getBlacklistId());


        rowID = database.insert(DatabaseContract.CONTACTS.TABLE, null, cv);
        if (rowID == -1){
            callback.notSuccessfull();
        }else {
            Log.d("LOG_TAG", "row inserted, ID = " + rowID);

            ArrayList<Contact> contacts = new ArrayList<>();
            Cursor c;
            String sqlQuery = "select * "
                    + "from " + DatabaseContract.CONTACTS.TABLE
                    + " where " + DatabaseContract.CONTACTS.COLUMN_USER_ID + " = ?";

            c = database.rawQuery(sqlQuery, new String[] {String.valueOf(userId)});

            int IdColIndex = c.getColumnIndex(DatabaseContract.CONTACTS.COLUMN_ID);
            int userIdIndex = c.getColumnIndex(DatabaseContract.CONTACTS.COLUMN_USER_ID);
            int nameIndex = c.getColumnIndex(DatabaseContract.CONTACTS.COLUMN_NAME);
            int numberIndex = c.getColumnIndex(DatabaseContract.CONTACTS.COLUMN_NUMBER);
            int emailIndex = c.getColumnIndex(DatabaseContract.CONTACTS.COLUMN_EMAIL);
            int addressIndex = c.getColumnIndex(DatabaseContract.CONTACTS.COLUMN_ADDRESS);
            int companyIndex = c.getColumnIndex(DatabaseContract.CONTACTS.COLUMN_COMPANY);
            int photoUrlIndex = c.getColumnIndex(DatabaseContract.CONTACTS.COLUMN_PHOTO_URL);
            int isFavoriteIndex = c.getColumnIndex(DatabaseContract.CONTACTS.COLUMN_IS_FAVORITE);

            if(c.moveToFirst()){
                do {
                    Contact con = new Contact();
                    Log.d("CONTACT ID - ",""+c.getInt(IdColIndex));
                    con.setId(c.getInt(IdColIndex));

                    Log.d("USER ID - ",c.getInt(userIdIndex)+"");

                    Log.d("CONTACT NAME - ",""+c.getString(nameIndex));
                    con.setName(c.getString(nameIndex));

                    Log.d("CONTACT numberIndex - ",""+c.getString(numberIndex));
                    con.setNumber(c.getString(numberIndex));

                    Log.d("CONTACT emailIndex - ",""+c.getString(emailIndex));
                    con.setEmail(c.getString(emailIndex));

                    Log.d("CONTACT addressIndex - ",""+c.getString(addressIndex));
                    con.setAddress(c.getString(addressIndex));

                    Log.d("CONTACT companyIndex - ",""+c.getString(companyIndex));
                    con.setCompany(c.getString(companyIndex));

                    Log.d("CONTACT UrlIndex - ",""+c.getString(photoUrlIndex));
                    con.setPhotoUrl(c.getString(photoUrlIndex));

                    Log.d("CONTACT isFavorite - ",""+c.getString(isFavoriteIndex));
                    if (c.getString(isFavoriteIndex).equals("true")){
                        con.setFavorite(true);
                    }else if(c.getString(isFavoriteIndex).equals("false")){
                        con.setFavorite(false);
                    }

                }while (c.moveToNext());
            }

            callback.addedSuccessfull();
        }
        database.close();
        dbHelper.close();
    }
}
