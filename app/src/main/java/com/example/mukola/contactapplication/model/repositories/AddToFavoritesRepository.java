package com.example.mukola.contactapplication.model.repositories;

import android.support.annotation.NonNull;

public interface AddToFavoritesRepository {

    void addToFavorites(@NonNull int userId,@NonNull String contactId, @NonNull addToFavoritesCallback callback);

    public interface addToFavoritesCallback {

        void addedSuccessfull();

        void notSuccessfull();
    }
}
