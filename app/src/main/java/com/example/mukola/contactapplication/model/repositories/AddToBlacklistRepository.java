package com.example.mukola.contactapplication.model.repositories;

import android.support.annotation.NonNull;

public interface AddToBlacklistRepository {
    void addToBlacklist(@NonNull int userId, @NonNull String contactId, @NonNull AddToBlacklistCallback callback);

    public interface AddToBlacklistCallback {

        void addedSuccessfull();

        void notSuccessfull();
    }
}
