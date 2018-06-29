package com.example.mukola.contactapplication.model.repositories;

import android.support.annotation.NonNull;

public interface DeleteFromFavoritesRepository {
    void deleteFromFavorites(@NonNull int userId, @NonNull String contactId, @NonNull deleteFromFavoritesCallback callback);

    public interface deleteFromFavoritesCallback {

        void deletedSuccessfull();

        void notSuccessfull();
    }
}
