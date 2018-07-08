package com.example.mukola.contactapplication.model.repositories;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.mukola.contactapplication.model.database.DatabaseContract;
import com.example.mukola.contactapplication.model.database.DatabaseHelper;

import java.util.ArrayList;

public class GetBlacklistRepositoryImpl implements GetBlacklistRepository {

    @NonNull
    private Context context;

    public GetBlacklistRepositoryImpl(@NonNull Context context) {
        this.context = context;
    }

    @Override
    public void getBlacklist(@NonNull int userId, @NonNull GetBlacklistCallback callback) {
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        ArrayList<String> blacklist = new ArrayList<>();
        Cursor c;
        String sqlQuery = "select * "
                + "from " + DatabaseContract.BLACKLIST.TABLE
                + " where " + DatabaseContract.BLACKLIST.COLUMN_USER_ID + " = ?";

        c = database.rawQuery(sqlQuery, new String[] {String.valueOf(userId)});

        int IdColIndex = c.getColumnIndex(DatabaseContract.BLACKLIST.COLUMN_ID);
        int userIdIndex = c.getColumnIndex(DatabaseContract.BLACKLIST.COLUMN_USER_ID);
        int personIndex = c.getColumnIndex(DatabaseContract.BLACKLIST.COLUMN_PERSON_ID);


        if(c.moveToFirst()){
            do {
                Log.d("ID - ",""+c.getInt(IdColIndex));


                Log.d("USER ID - ",c.getInt(userIdIndex)+"");

                Log.d("Person ID  - ",""+c.getString(personIndex));
                blacklist.add(c.getString(personIndex));


            }while (c.moveToNext());

            callback.onBlacklistGet(blacklist);
        }else {
            callback.notFound();
        }

        database.close();
        dbHelper.close();
    }
}
