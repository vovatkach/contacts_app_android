package com.example.mukola.contactapplication.model.repositories;

import android.support.annotation.NonNull;

import com.example.mukola.contactapplication.model.models.User;

public interface GetUserRepository {

    void getUser(@NonNull String email, @NonNull GetUserCallback callback);

    public interface GetUserCallback {

        void foundUser(@NonNull User user);

        void notFound();
    }

}