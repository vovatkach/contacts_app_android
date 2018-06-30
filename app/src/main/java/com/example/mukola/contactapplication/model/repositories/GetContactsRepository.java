package com.example.mukola.contactapplication.model.repositories;

import android.support.annotation.NonNull;

import com.example.mukola.contactapplication.model.models.Contact;

import java.util.ArrayList;

public interface GetContactsRepository {
    void getContacts(@NonNull int userId, @NonNull GetContactsCallback callback);

    public interface GetContactsCallback {

        void onContactsGet(@NonNull ArrayList<Contact> list);

        void notFound();
    }
}
