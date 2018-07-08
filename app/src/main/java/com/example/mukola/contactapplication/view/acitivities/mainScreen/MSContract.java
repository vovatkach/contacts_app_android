package com.example.mukola.contactapplication.view.acitivities.mainScreen;

import android.support.annotation.NonNull;

import com.example.mukola.contactapplication.model.models.Contact;

import java.util.List;

public interface MSContract {
    public interface IMainScreenView{

        void showToast(@NonNull String message);


        void openContact(@NonNull Contact contact,int userId);


        void openCleanUp();

        void openCreateContact();

        void openCityReminder();

        void setImportButtonVisible();

        void setContactList(List<Contact> contacts);

        void openFavorite();

    }

    public interface IMainScreenPresenter{

        void detachView();


        void openContact(@NonNull Contact contact, int userId);


        void openCleanUp();

        void openCreateContact();

        void openCityReminder();

        void getContacts(@NonNull int userId);

        void openFavorite();

        void addToFavorite(@NonNull int userId,@NonNull Contact contact);

        void deleteFRomFavorite(@NonNull int userId,@NonNull Contact contact);

    }
}
