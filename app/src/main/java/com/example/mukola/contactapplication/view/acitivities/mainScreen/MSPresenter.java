package com.example.mukola.contactapplication.view.acitivities.mainScreen;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;

import com.example.mukola.contactapplication.model.models.Contact;
import com.example.mukola.contactapplication.model.repositories.GetContactsRepository;
import com.example.mukola.contactapplication.model.repositories.GetContactsRepositoryImpl;

import java.util.ArrayList;

public class MSPresenter implements MSContract.IMainScreenPresenter{

    @NonNull
    private MSContract.IMainScreenView view;

    @NonNull
    Activity activity;


    @NonNull
    GetContactsRepository getContactsRepository;



    public MSPresenter(@NonNull MSContract.IMainScreenView view, @NonNull Activity activity,@NonNull Context context){
        this.view = view;
        this.activity = activity;
        getContactsRepository = new GetContactsRepositoryImpl(context);

    }

    @Override
    public void detachView() {
        view = null;
    }


    @Override
    public void openContact(@NonNull Contact contact, int userId) {
        view.openContact(contact,userId);
    }

    @Override
    public void openCleanUp() {
        view.openCleanUp();
    }

    @Override
    public void openCreateContact() {
        view.openCreateContact();
    }

    @Override
    public void openCityReminder() {
        view.openCityReminder();
    }


    @Override
    public void getContacts(@NonNull int userId) {
        getContactsRepository.getContacts(userId, new GetContactsRepository.GetContactsCallback() {
            @Override
            public void onContactsGet(@NonNull final ArrayList<Contact> list) {
                activity.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        view.setContactList(list);
                    }
                });

            }

            @Override
            public void notFound() {
                view.setImportButtonVisible();
            }
        });
    }

    @Override
    public void openFavorite() {
        view.openFavorite();
    }

}
