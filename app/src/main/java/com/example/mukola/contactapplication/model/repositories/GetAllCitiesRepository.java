package com.example.mukola.contactapplication.model.repositories;

import android.support.annotation.NonNull;

import java.util.ArrayList;

public interface GetAllCitiesRepository {
    void getCities(@NonNull int userId, @NonNull GetCitiesCallback callback);

    public interface GetCitiesCallback {

        void onCitiesGet(@NonNull ArrayList<String> list);

        void notFound();
    }
}
