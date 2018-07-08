package com.example.mukola.contactapplication.model.repositories;

import android.support.annotation.NonNull;

public interface DeleteFromBlacklistRepository {
    void deleteFromBlacklist(@NonNull int userId, @NonNull String contactId, @NonNull deleteFromBlacklistCallback callback);

    public interface deleteFromBlacklistCallback {

        void deletedSuccessfull();

        void notSuccessfull();
    }
}
