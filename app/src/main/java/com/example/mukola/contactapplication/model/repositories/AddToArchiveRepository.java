package com.example.mukola.contactapplication.model.repositories;

import android.support.annotation.NonNull;

import com.example.mukola.contactapplication.model.models.Contact;

public interface AddToArchiveRepository {
    void addToArchive(@NonNull int userId, @NonNull Contact contact, @NonNull addToArchiveCallback callback);

    public interface addToArchiveCallback {

        void addedSuccessfull();

        void notSuccessfull();
    }
}
