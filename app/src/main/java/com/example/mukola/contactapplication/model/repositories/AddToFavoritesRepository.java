package com.example.mukola.contactapplication.model.repositories;

import android.support.annotation.NonNull;


public interface AddToFavoritesRepository {
    void addToFavorites(@NonNull int userId, @NonNull int contactId, @NonNull AddToFavoritesCallback callback);

    public interface AddToFavoritesCallback {

        void addedSuccessfull();

        void notSuccessfull();
    }
}
