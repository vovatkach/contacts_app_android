package com.example.mukola.contactapplication.model.repositories;

import android.support.annotation.NonNull;

public interface DeleteFromArchiveRepository {
    void deleteFromArchive(@NonNull int userId, @NonNull int contactId, @NonNull DeleteFromArchiveCallback callback);

    public interface DeleteFromArchiveCallback {

        void deletedSuccessfull();

        void notSuccessfull();
    }
}
