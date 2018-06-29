package com.example.mukola.contactapplication.model.repositories;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import com.example.mukola.contactapplication.model.database.DatabaseContract;
import com.example.mukola.contactapplication.model.database.DatabaseHelper;

import java.util.ArrayList;

public class GetFavoritesRepositoryImpl implements GetFavoritesRepository{

    @NonNull
    private Context context;

    public GetFavoritesRepositoryImpl(@NonNull Context context) {
        this.context = context;
    }

    @Override
    public void getFavorites(@NonNull int userId, @NonNull GetFavoritesCallback callback) {
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        ArrayList<String> favorites = new ArrayList<>();
        Cursor c;
        String sqlQuery = "select * "
                + "from " + DatabaseContract.FAVORITES.TABLE
                + " where " + DatabaseContract.FAVORITES.COLUMN_USER_ID + " = ?";

        c = database.rawQuery(sqlQuery, new String[] {String.valueOf(userId)});

        int contactIdColIndex = c.getColumnIndex(DatabaseContract.FAVORITES.COLUMN_CONTACT_ID);

        if(c.moveToFirst()){
            do {

                favorites.add(c.getString(contactIdColIndex));

            }while (c.moveToNext());
        }
        if (!favorites.isEmpty()){
            callback.onFavoritesGet(favorites);
        }else{
            callback.notFound();
        }

        c.close();
        database.close();
        dbHelper.close();
    }
}
