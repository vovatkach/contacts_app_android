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

public class GetArchiveRepositoryImpl implements GetArchiveRepository {

    @NonNull
    private Context context;

    public GetArchiveRepositoryImpl(@NonNull Context context) {
        this.context = context;
    }

    @Override
    public void getArchive(@NonNull int userId, @NonNull GetArchiveCallback callback) {
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        ArrayList<Contact> contacts = new ArrayList<>();
        Cursor c;
        String sqlQuery = "select * "
                + "from " + DatabaseContract.ARCHIVED.TABLE
                + " where " + DatabaseContract.ARCHIVED.COLUMN_USER_ID + " = ?";

        c = database.rawQuery(sqlQuery, new String[] {String.valueOf(userId)});

        int IdColIndex = c.getColumnIndex(DatabaseContract.ARCHIVED.COLUMN_ID);
        int userIdIndex = c.getColumnIndex(DatabaseContract.ARCHIVED.COLUMN_USER_ID);
        int nameIndex = c.getColumnIndex(DatabaseContract.ARCHIVED.COLUMN_NAME);
        int numberIndex = c.getColumnIndex(DatabaseContract.ARCHIVED.COLUMN_NUMBER);
        int emailIndex = c.getColumnIndex(DatabaseContract.ARCHIVED.COLUMN_EMAIL);
        int addressIndex = c.getColumnIndex(DatabaseContract.ARCHIVED.COLUMN_ADDRESS);
        int companyIndex = c.getColumnIndex(DatabaseContract.ARCHIVED.COLUMN_COMPANY);
        int photoUrlIndex = c.getColumnIndex(DatabaseContract.ARCHIVED.COLUMN_PHOTO_URL);
        int isFavoriteIndex = c.getColumnIndex(DatabaseContract.ARCHIVED.COLUMN_IS_FAVORITE);
        int blacklistIndex = c.getColumnIndex(DatabaseContract.ARCHIVED.COLUMN_BLACKLIST_ID);


        if(c.moveToFirst()){
            do {
                Contact con = new Contact();
                Log.d("A CONTACT ID - ",""+c.getInt(IdColIndex));
                con.setId(c.getInt(IdColIndex));

                Log.d("A USER ID - ",c.getInt(userIdIndex)+"");

                Log.d("A CONTACT NAME - ",""+c.getString(nameIndex));
                con.setName(c.getString(nameIndex));

                Log.d("A CONTACT numbIndex - ",""+c.getString(numberIndex));
                con.setNumber(c.getString(numberIndex));

                Log.d("A CONTACT emailIndex - ",""+c.getString(emailIndex));
                con.setEmail(c.getString(emailIndex));

                Log.d("A CONTACT addrIndex - ",""+c.getString(addressIndex));
                con.setAddress(c.getString(addressIndex));

                Log.d("A CONTACT compaIndex - ",""+c.getString(companyIndex));
                con.setCompany(c.getString(companyIndex));

                Log.d("A CONTACT UrlIndex - ",""+c.getString(photoUrlIndex));
                con.setPhotoUrl(c.getString(photoUrlIndex));

                Log.d("A CONTACT isFavorite - ",""+c.getString(isFavoriteIndex));
                if (c.getString(isFavoriteIndex).equals("true")){
                    con.setFavorite(true);
                }else if(c.getString(isFavoriteIndex).equals("false")){
                    con.setFavorite(false);
                }

                Log.d("CONTACT blacklistId - ",""+c.getString(blacklistIndex));
                con.setBlacklistId(c.getString(blacklistIndex));

                contacts.add(con);

            }while (c.moveToNext());

            callback.onArchiveGet(contacts);
        }else {
            callback.notFound();
        }

        database.close();
        dbHelper.close();
    }
}
