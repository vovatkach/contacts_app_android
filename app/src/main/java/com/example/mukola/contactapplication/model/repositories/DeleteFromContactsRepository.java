package com.example.mukola.contactapplication.model.repositories;

import android.support.annotation.NonNull;

public interface DeleteFromContactsRepository {
    void deleteFromContacts(@NonNull int userId,@NonNull int contactId, @NonNull DeleteFromContactsCallback callback);

    public interface DeleteFromContactsCallback {

        void deletedSuccessfull();

        void notSuccessfull();
    }
}
