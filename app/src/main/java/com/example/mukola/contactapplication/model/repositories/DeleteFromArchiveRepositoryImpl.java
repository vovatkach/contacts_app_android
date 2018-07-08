package com.example.mukola.contactapplication.model.repositories;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.mukola.contactapplication.model.database.DatabaseContract;
import com.example.mukola.contactapplication.model.database.DatabaseHelper;

public class DeleteFromArchiveRepositoryImpl implements DeleteFromArchiveRepository {


    @NonNull
    private Context context;

    public DeleteFromArchiveRepositoryImpl(@NonNull Context context) {
        this.context = context;
    }

    @Override
    public void deleteFromArchive(@NonNull int userId, @NonNull int contactId, @NonNull DeleteFromArchiveCallback callback) {
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        Log.d("DELETING", "--- Delete from ARCHIVE: ---" + userId);
        // удаляем по id
        int delCount = database.delete(DatabaseContract.ARCHIVED.TABLE,
                DatabaseContract.ARCHIVED.COLUMN_ID + " = "
                        +contactId + " and " + DatabaseContract.ARCHIVED.COLUMN_USER_ID
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
