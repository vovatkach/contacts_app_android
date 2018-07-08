package com.example.mukola.contactapplication.model.repositories;

import android.support.annotation.NonNull;

import com.example.mukola.contactapplication.model.models.Contact;

import java.util.ArrayList;

public interface GetArchiveRepository {
    void getArchive(@NonNull int userId, @NonNull GetArchiveCallback callback);

    public interface GetArchiveCallback {

        void onArchiveGet(@NonNull ArrayList<Contact> list);

        void notFound();
    }
}
