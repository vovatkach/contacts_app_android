package com.example.mukola.contactapplication.model.repositories;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.mukola.contactapplication.model.database.DatabaseContract;
import com.example.mukola.contactapplication.model.database.DatabaseHelper;
import com.example.mukola.contactapplication.model.models.User;

public class RegisterRepositoryImpl implements RegisterRepository {

    @NonNull
    private Context context;

    public RegisterRepositoryImpl(@NonNull Context context) {
        this.context = context;
    }

    @Override
    public void register(@NonNull User user, @NonNull RegisterCallback registerCallback) {
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        Log.d("User name",user.getName());

        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseContract.USERS.COLUMN_NAME, user.getName());
        contentValues.put(DatabaseContract.USERS.COLUMN_PASSWORD, user.getPassword());
        contentValues.put(DatabaseContract.USERS.COLUMN_NUMBER, user.getNumber());
        contentValues.put(DatabaseContract.USERS.COLUMN_ADDRESS, user.getAddress());
        contentValues.put(DatabaseContract.USERS.COLUMN_EMAIL, user.getEmail());

        long k = database.insert(DatabaseContract.USERS.TABLE, null, contentValues);

        user.setId( (int) k);

        Log.d("row inserted - ",""+k);


        database.close();
        dbHelper.close();
        registerCallback.register(user);
    }
}
