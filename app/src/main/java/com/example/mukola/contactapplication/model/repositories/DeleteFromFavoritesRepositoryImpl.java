package com.example.mukola.contactapplication.model.repositories;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.mukola.contactapplication.model.database.DatabaseContract;
import com.example.mukola.contactapplication.model.database.DatabaseHelper;

public class DeleteFromFavoritesRepositoryImpl implements DeleteFromFavoritesRepository {
    @NonNull
    private Context context;

    public DeleteFromFavoritesRepositoryImpl(@NonNull Context context) {
        this.context = context;
    }


    @Override
    public void deleteFromFavorites(@NonNull int userId, @NonNull int contactId,
                                    @NonNull deleteFromFavoritesCallback callback) {
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        SQLiteDatabase database = dbHelper.getWritableDatabase();


        long rowID = 0;
        ContentValues cv = new ContentValues();
        Log.d("LOG_TAG", "--- Insert in usersExtra: ---");

        Log.d("LOG_TAG", "idFavorite = " + "false");
        cv.put(DatabaseContract.CONTACTS.COLUMN_IS_FAVORITE, "false");

        rowID =    database.update(DatabaseContract.CONTACTS.TABLE, cv, "id" + "=" + String.valueOf(contactId), null);
        Log.d("LOG_TAG", "row deleted, ID = " + rowID);

        database.close();
        dbHelper.close();
    }
}
