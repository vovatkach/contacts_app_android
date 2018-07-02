package com.example.mukola.contactapplication.view.acitivities.mainScreen;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.mukola.contactapplication.model.models.Contact;
import com.example.mukola.contactapplication.model.peopleHelper.PeopleHelper;
import com.example.mukola.contactapplication.model.repositories.GetContactsRepository;
import com.example.mukola.contactapplication.model.repositories.GetContactsRepositoryImpl;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.api.services.people.v1.People;
import com.google.api.services.people.v1.model.ListConnectionsResponse;
import com.google.api.services.people.v1.model.Person;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class MSPresenter implements MSContract.IMainScreenPresenter{

    @NonNull
    private MSContract.IMainScreenView view;

    @NonNull
    Activity activity;




    public MSPresenter(@NonNull MSContract.IMainScreenView view, @NonNull Activity activity){
        this.view = view;
        this.activity = activity;
    }

    @Override
    public void detachView() {
        view = null;
    }

    @Override
    public void openAllContacts() {
        view.openAllContacts();
    }

    @Override
    public void openContact(@NonNull Contact contact, int userId) {
        view.openContact(contact,userId);
    }

    @Override
    public void openFavorite( int userId) {
        view.openFavorite(userId);
    }

    @Override
    public void openImport() {
        view.openImport();
    }

    @Override
    public void openCreateContact() {
        view.openCreateContact();
    }

    @Override
    public void openCityReminder() {
        view.openCityReminder();
    }


}
