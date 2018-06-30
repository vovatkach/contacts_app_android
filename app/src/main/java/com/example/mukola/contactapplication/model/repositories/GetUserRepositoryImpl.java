package com.example.mukola.contactapplication.model.repositories;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.mukola.contactapplication.model.database.DatabaseContract;
import com.example.mukola.contactapplication.model.database.DatabaseHelper;
import com.example.mukola.contactapplication.model.models.User;

import java.util.ArrayList;

public class GetUserRepositoryImpl implements GetUserRepository {

    @NonNull
    private Context context;

    public GetUserRepositoryImpl(@NonNull Context context) {
        this.context = context;
    }

    @Override
    public void getUser(@NonNull String email, @NonNull GetUserCallback callback) {
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        String selection = DatabaseContract.USERS.COLUMN_EMAIL + " = ?";
        String[] selectionArgs = new String[]{email};

        Cursor cursor = database.query(DatabaseContract.USERS.TABLE, null, selection, selectionArgs, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                User user = new User();

                int idIndex = cursor.getColumnIndex(DatabaseContract.USERS.COLUMN_ID);
                user.setId(cursor.getInt(idIndex));

                int usernameIndex = cursor.getColumnIndex(DatabaseContract.USERS.COLUMN_NAME);
                user.setName(cursor.getString(usernameIndex));

                int passwordIndex = cursor.getColumnIndex(DatabaseContract.USERS.COLUMN_PASSWORD);
                user.setPassword(cursor.getString(passwordIndex));

                int numberIndex = cursor.getColumnIndex(DatabaseContract.USERS.COLUMN_NUMBER);
                user.setNumber(cursor.getString(numberIndex));

                int addressIndex = cursor.getColumnIndex(DatabaseContract.USERS.COLUMN_ADDRESS);
                user.setAddress(cursor.getString(addressIndex));

                int emailIndex = cursor.getColumnIndex(DatabaseContract.USERS.COLUMN_EMAIL);
                user.setEmail(cursor.getString(emailIndex));

                callback.foundUser(user);

                Log.d("FOUND USER", user.getEmail());
            } else {
                callback.notFound();
            }
        } else {
            callback.notFound();
        }
        if (cursor != null) {
            cursor.close();
        }

        database.close();
        dbHelper.close();
    }


}