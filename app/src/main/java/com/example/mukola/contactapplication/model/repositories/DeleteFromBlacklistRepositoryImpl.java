package com.example.mukola.contactapplication.model.repositories;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.mukola.contactapplication.model.database.DatabaseContract;
import com.example.mukola.contactapplication.model.database.DatabaseHelper;

public class DeleteFromBlacklistRepositoryImpl implements DeleteFromBlacklistRepository {


    @NonNull
    private Context context;

    public DeleteFromBlacklistRepositoryImpl(@NonNull Context context) {
        this.context = context;
    }

    @Override
    public void deleteFromBlacklist(@NonNull int userId, @NonNull String contactId, @NonNull deleteFromBlacklistCallback callback) {
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        Log.d("DELETING", "--- Delete from Blacklist: ---" + userId);
        // удаляем по id
        int delCount = database.delete(DatabaseContract.BLACKLIST.TABLE,
                DatabaseContract.BLACKLIST.COLUMN_PERSON_ID + " = "
                        + "'" + contactId + "'" + " and " + DatabaseContract.BLACKLIST.COLUMN_USER_ID
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
