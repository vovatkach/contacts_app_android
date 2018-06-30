package com.example.mukola.contactapplication.model.repositories;

import android.support.annotation.NonNull;

import com.example.mukola.contactapplication.model.models.Contact;

public interface UpdateContactRepository {
    void updateContact(@NonNull int userId, @NonNull Contact contact, @NonNull updateContactCallback callback);

    public interface updateContactCallback {

        void updatedSuccessfull();

        void notSuccessfull();
    }
}
