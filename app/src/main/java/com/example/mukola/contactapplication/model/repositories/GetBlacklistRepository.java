package com.example.mukola.contactapplication.model.repositories;

import android.support.annotation.NonNull;

import java.util.ArrayList;

public interface GetBlacklistRepository {
    void getBlacklist(@NonNull int userId, @NonNull GetBlacklistCallback callback);

    public interface GetBlacklistCallback {

        void onBlacklistGet(@NonNull ArrayList<String> list);

        void notFound();
    }
}
