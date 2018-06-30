package com.example.mukola.contactapplication.model.repositories;

import android.support.annotation.NonNull;

import com.example.mukola.contactapplication.model.models.Contact;

public interface AddToContactsRepository {

    void addToContacts(@NonNull int userId, @NonNull Contact contact, @NonNull addToContactsCallback callback);

    public interface addToContactsCallback {

        void addedSuccessfull();

        void notSuccessfull();
    }
}
