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

import java.util.ArrayList;

public class AddToFavoritesRepositoryImpl implements AddToFavoritesRepository{

    @NonNull
    private Context context;

    public AddToFavoritesRepositoryImpl(@NonNull Context context) {
        this.context = context;
    }

    @Override
    public void addToFavorites(@NonNull int userId, @NonNull String contactId, @NonNull addToFavoritesCallback callback) {
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        long rowID = 0;
        ContentValues cv = new ContentValues();
        Log.d("LOG_TAG", "--- Insert in usersExtra: ---");
        // подготовим данные для вставки в виде пар: наименование столбца -
        // значение
        Log.d("LOG_TAG", "userId = " + userId);
        cv.put(DatabaseContract.FAVORITES.COLUMN_USER_ID, userId);

        Log.d("LOG_TAG", "contactId = " + contactId);
        cv.put(DatabaseContract.FAVORITES.COLUMN_CONTACT_ID, contactId);

        rowID = database.insert(DatabaseContract.FAVORITES.TABLE, null, cv);
        if (rowID == -1){
            callback.notSuccessfull();
        }else {
            Log.d("LOG_TAG", "row inserted, ID = " + rowID);

            ArrayList<String> favorites = new ArrayList<>();
            Cursor c;
            String sqlQuery = "select * "
                    + "from " + DatabaseContract.FAVORITES.TABLE
                    + " where " + DatabaseContract.FAVORITES.COLUMN_USER_ID + " = ?";

            c = database.rawQuery(sqlQuery, new String[] {String.valueOf(userId)});

            int contactIdColIndex = c.getColumnIndex(DatabaseContract.FAVORITES.COLUMN_CONTACT_ID);
            int IdColIndex = c.getColumnIndex(DatabaseContract.FAVORITES.COLUMN_USER_ID);


            if(c.moveToFirst()){
                do {

                    favorites.add(c.getString(contactIdColIndex));
                    Log.d("HUY",c.getString(contactIdColIndex));
                    Log.d("HUYUSER",c.getInt(IdColIndex)+"");

                }while (c.moveToNext());
            }

            callback.addedSuccessfull();
        }
        database.close();
        dbHelper.close();
    }
}
