package com.example.mukola.contactapplication.model.repositories;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.mukola.contactapplication.model.database.DatabaseContract;
import com.example.mukola.contactapplication.model.database.DatabaseHelper;

public class DeleteFromFavoritesRepositoryImpl implements DeleteFromFavoritesRepository {
    @NonNull
    private Context context;

    public DeleteFromFavoritesRepositoryImpl(@NonNull Context context) {
        this.context = context;
    }


    @Override
    public void deleteFromFavorites(@NonNull int userId, @NonNull String contactId,
                                    @NonNull deleteFromFavoritesCallback callback) {
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        Log.d("DELETING", "--- Delete from Achievements: ---" + userId + " " + userId);
        // удаляем по id
        int delCount = database.delete(DatabaseContract.FAVORITES.TABLE,
                DatabaseContract.FAVORITES.COLUMN_CONTACT_ID + " = "
                        + "'"+contactId +"'"+ " and " + DatabaseContract.FAVORITES.COLUMN_USER_ID
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
