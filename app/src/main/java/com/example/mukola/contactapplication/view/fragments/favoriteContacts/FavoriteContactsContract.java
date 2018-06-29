package com.example.mukola.contactapplication.view.fragments.favoriteContacts;

import android.support.annotation.NonNull;

import com.google.api.services.people.v1.model.Person;

import java.util.ArrayList;
import java.util.List;

public interface FavoriteContactsContract {
    public interface IFavoriteContactsView{

        void setProgressBarVisible();

        void sendMessage(String number);

        void makeCall(String number);

        void onContactClicked(Person person);

        void setContactList(List<Person> contacts);

        void showToast(@NonNull String message);

    }

    public interface IFavoriteContactsPresenter{

        void detachView();

        void sendMessage(String number);

        void makeCall(String number);

        void onRequestPermissionsResult(int requestCode,
                                        String permissions[], int[] grantResults,String number);

        boolean checkAndRequestPermissions(int permissionCode);

        void onContactClicked(Person person);

        void getFavorites(int userId);

        void checkFavorites(ArrayList<Person> list);

    }
}
