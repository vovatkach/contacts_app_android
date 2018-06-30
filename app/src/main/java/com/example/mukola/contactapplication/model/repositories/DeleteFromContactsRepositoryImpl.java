package com.example.mukola.contactapplication.model.repositories;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.mukola.contactapplication.model.database.DatabaseContract;
import com.example.mukola.contactapplication.model.database.DatabaseHelper;

public class DeleteFromContactsRepositoryImpl implements DeleteFromContactsRepository {

    @NonNull
    private Context context;

    public DeleteFromContactsRepositoryImpl(@NonNull Context context) {
        this.context = context;
    }


    @Override
    public void deleteFromContacts( @NonNull int userId,@NonNull int contactId,
                                    @NonNull DeleteFromContactsCallback callback) {
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        Log.d("DELETING", "--- Delete from Contacts: ---" + userId);
        // удаляем по id
        int delCount = database.delete(DatabaseContract.CONTACTS.TABLE,
                DatabaseContract.CONTACTS.COLUMN_ID + " = "
                        +contactId + " and " + DatabaseContract.CONTACTS.COLUMN_USER_ID
                        + " = " +userId, null);
        Log.d("DELETING", "deleted rows count = " + delCount);

        if (delCount>0){
            callback.deletedSuccessfull();
        }else {
            callback.notSuccessfull();
        }

        database.close();
        dbHelper.close();
    }
}
