package com.example.mukola.contactapplication.view.acitivities.mainScreen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.example.mukola.contactapplication.model.models.Contact;
import com.example.mukola.contactapplication.model.models.User;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.api.services.people.v1.model.Person;

import java.util.ArrayList;
import java.util.List;

public interface MSContract {
    public interface IMainScreenView{

        void showToast(@NonNull String message);

        void openAllContacts();

        void openContact(@NonNull Contact contact,int userId);

        void openFavorite(@NonNull int userId);

        void openImport();

        void openCreateContact();
    }

    public interface IMainScreenPresenter{

        void detachView();

        void openAllContacts();

        void openContact(@NonNull Contact contact, int userId);

        void openFavorite(@NonNull int userId);

        void openImport();

        void openCreateContact();

    }
}
