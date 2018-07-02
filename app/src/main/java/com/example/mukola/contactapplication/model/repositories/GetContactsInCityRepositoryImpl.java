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

public class GetContactsInCityRepositoryImpl implements GetContactsInCityRepository {

    @NonNull
    private Context context;

    public GetContactsInCityRepositoryImpl(@NonNull Context context) {
        this.context = context;
    }

    @Override
    public void getContacts(@NonNull int userId,@NonNull String city, @NonNull GetContactsCallback callback) {
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        ArrayList<Contact> contacts = new ArrayList<>();
        Cursor c;
        String sqlQuery = "select * "
                + "from " + DatabaseContract.CONTACTS.TABLE
                + " where " + DatabaseContract.CONTACTS.COLUMN_USER_ID + " = ?" + " and " +
                DatabaseContract.CONTACTS.COLUMN_ADDRESS + " = ?";

        c = database.rawQuery(sqlQuery, new String[]{String.valueOf(userId),city});

        int IdColIndex = c.getColumnIndex(DatabaseContract.CONTACTS.COLUMN_ID);
        int userIdIndex = c.getColumnIndex(DatabaseContract.CONTACTS.COLUMN_USER_ID);
        int nameIndex = c.getColumnIndex(DatabaseContract.CONTACTS.COLUMN_NAME);
        int numberIndex = c.getColumnIndex(DatabaseContract.CONTACTS.COLUMN_NUMBER);
        int emailIndex = c.getColumnIndex(DatabaseContract.CONTACTS.COLUMN_EMAIL);
        int addressIndex = c.getColumnIndex(DatabaseContract.CONTACTS.COLUMN_ADDRESS);
        int companyIndex = c.getColumnIndex(DatabaseContract.CONTACTS.COLUMN_COMPANY);
        int photoUrlIndex = c.getColumnIndex(DatabaseContract.CONTACTS.COLUMN_PHOTO_URL);
        int isFavoriteIndex = c.getColumnIndex(DatabaseContract.CONTACTS.COLUMN_IS_FAVORITE);

        if (c.moveToFirst()) {
            do {
                Contact con = new Contact();
                Log.d("CONTACT ID - ", "" + c.getInt(IdColIndex));
                con.setId(c.getInt(IdColIndex));

                Log.d("USER ID - ", c.getInt(userIdIndex) + "");

                Log.d("CONTACT NAME - ", "" + c.getString(nameIndex));
                con.setName(c.getString(nameIndex));

                Log.d("CONTACT numberIndex - ", "" + c.getString(numberIndex));
                con.setNumber(c.getString(numberIndex));

                Log.d("CONTACT emailIndex - ", "" + c.getString(emailIndex));
                con.setEmail(c.getString(emailIndex));

                Log.d("CONTACT addressIndex - ", "" + c.getString(addressIndex));
                con.setAddress(c.getString(addressIndex));

                Log.d("CONTACT companyIndex - ", "" + c.getString(companyIndex));
                con.setCompany(c.getString(companyIndex));

                Log.d("CONTACT UrlIndex - ", "" + c.getString(photoUrlIndex));
                con.setPhotoUrl(c.getString(photoUrlIndex));

                Log.d("CONTACT isFavorite - ", "" + c.getString(isFavoriteIndex));
                if (c.getString(isFavoriteIndex).equals("true")) {
                    con.setFavorite(true);
                } else if (c.getString(isFavoriteIndex).equals("false")) {
                    con.setFavorite(false);
                }

                contacts.add(con);

            } while (c.moveToNext());

            callback.onContactsGet(contacts);
        }else{
            callback.notFound();
        }

        database.close();
        dbHelper.close();
    }
}
