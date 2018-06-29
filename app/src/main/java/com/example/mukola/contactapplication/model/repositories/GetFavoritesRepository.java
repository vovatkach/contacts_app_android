package com.example.mukola.contactapplication.model.repositories;

import android.support.annotation.NonNull;

import java.util.ArrayList;

public interface GetFavoritesRepository {
    void getFavorites(@NonNull int userId, @NonNull GetFavoritesCallback callback);

    public interface GetFavoritesCallback {

        void onFavoritesGet(@NonNull ArrayList<String> list);

        void notFound();
    }
}
