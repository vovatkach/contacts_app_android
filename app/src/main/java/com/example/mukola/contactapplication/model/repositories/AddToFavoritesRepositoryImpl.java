package com.example.mukola.contactapplication.model.repositories;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.mukola.contactapplication.model.database.DatabaseContract;
import com.example.mukola.contactapplication.model.database.DatabaseHelper;

public class AddToFavoritesRepositoryImpl implements AddToFavoritesRepository {
    @NonNull
    private Context context;

    public AddToFavoritesRepositoryImpl(@NonNull Context context) {
        this.context = context;
    }

    @Override
    public void addToFavorites(@NonNull int userId, @NonNull int contactId, @NonNull AddToFavoritesCallback callback) {

        DatabaseHelper dbHelper = new DatabaseHelper(context);
        SQLiteDatabase database = dbHelper.getWritableDatabase();


        long rowID;
        ContentValues cv = new ContentValues();
        Log.d("LOG_TAG", "--- Insert in favorites: ---");

        Log.d("LOG_TAG", "idFavorite = " + "true");
        cv.put(DatabaseContract.CONTACTS.COLUMN_IS_FAVORITE, "true");

        rowID =    database.update(DatabaseContract.CONTACTS.TABLE, cv, "id" + "=" + String.valueOf(contactId), null);
        Log.d("LOG_TAG", "row updated, ID = " + rowID);

        if (rowID>0){
            callback.addedSuccessfull();
        }else {
            callback.notSuccessfull();
        }


        database.close();
        dbHelper.close();
    }
}
