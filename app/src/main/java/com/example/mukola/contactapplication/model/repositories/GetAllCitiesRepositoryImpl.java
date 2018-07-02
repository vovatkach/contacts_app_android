package com.example.mukola.contactapplication.model.repositories;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.mukola.contactapplication.model.database.DatabaseContract;
import com.example.mukola.contactapplication.model.database.DatabaseHelper;
import com.example.mukola.contactapplication.model.models.Contact;

import java.util.ArrayList;

public class GetAllCitiesRepositoryImpl implements GetAllCitiesRepository {
    @NonNull
    private Context context;

    public GetAllCitiesRepositoryImpl(@NonNull Context context) {
        this.context = context;
    }

    @Override
    public void getCities(@NonNull int userId, @NonNull GetCitiesCallback callback) {
        Log.d("GETING","ALL CITIES");

        DatabaseHelper dbHelper = new DatabaseHelper(context);
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        ArrayList<String> cities = new ArrayList<>();
        Cursor c;
        String sqlQuery = "select " + DatabaseContract.CONTACTS.COLUMN_ADDRESS
                + " from " + DatabaseContract.CONTACTS.TABLE
                + " where " + DatabaseContract.CONTACTS.COLUMN_USER_ID + " = ?";


        c = database.rawQuery(sqlQuery, new String[]{String.valueOf(userId)});

        int cityIndex = c.getColumnIndex(DatabaseContract.CONTACTS.COLUMN_ADDRESS);

        if (c.moveToFirst()) {
            do {


                Log.d("CONTACT addressIndex - ", "" + c.getString(cityIndex));
                cities.add(c.getString(cityIndex));


            } while (c.moveToNext());

            callback.onCitiesGet(cities);
        }else{
            callback.notFound();
        }

        database.close();
        dbHelper.close();
    }
}
