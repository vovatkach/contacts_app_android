package com.example.mukola.contactapplication.model.repositories;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.mukola.contactapplication.model.database.DatabaseContract;
import com.example.mukola.contactapplication.model.database.DatabaseHelper;

public class AddToBlacklistRepositoryImpl implements AddToBlacklistRepository {

    @NonNull
    private Context context;

    public AddToBlacklistRepositoryImpl(@NonNull Context context) {
        this.context = context;
    }

    @Override
    public void addToBlacklist(@NonNull int userId, @NonNull String contactId, @NonNull AddToBlacklistCallback callback) {
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        long rowID = 0;
        ContentValues cv = new ContentValues();
        Log.d("LOG_TAG", "--- Insert in BLACKLIST: ---");
        // подготовим данные для вставки в виде пар: наименование столбца -
        // значение
        Log.d("LOG_TAG", "userId = " + userId);
        cv.put(DatabaseContract.BLACKLIST.COLUMN_USER_ID, userId);

        Log.d("LOG_TAG", "personId = " + contactId);
        cv.put(DatabaseContract.BLACKLIST.COLUMN_PERSON_ID, contactId);

        rowID = database.insert(DatabaseContract.BLACKLIST.TABLE, null, cv);
        if (rowID == -1){
            callback.notSuccessfull();
        }else {
            Log.d("LOG_TAG", "row inserted, ID = " + rowID);
            callback.addedSuccessfull();
        }

        dbHelper.close();
        database.close();
    }
}
