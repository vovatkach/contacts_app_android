package com.example.mukola.contactapplication.view.fragments.favoriteContacts;

import android.support.annotation.NonNull;

import com.example.mukola.contactapplication.model.models.Contact;
import com.google.api.services.people.v1.model.Person;

import java.util.ArrayList;
import java.util.List;

public interface FavoriteContactsContract {
    public interface IFavoriteContactsView{

        void sendMessage(@NonNull String number);

        void makeCall(@NonNull String number);

        void onContactClicked(@NonNull Contact contact);

        void setContactList(List<Contact> contacts);

        void showToast(@NonNull String message);

        void setvNoFavoriteVisible();

    }

    public interface IFavoriteContactsPresenter{

        void detachView();

        void sendMessage(@NonNull String number);

        void makeCall(@NonNull String number);

        void onRequestPermissionsResult(int requestCode,
                                        String permissions[], int[] grantResults,String number);

        boolean checkAndRequestPermissions(@NonNull int permissionCode);

        void onContactClicked(@NonNull Contact contact);

        void getFavorites(@NonNull int userId);
    }
}
