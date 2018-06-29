package com.example.mukola.contactapplication.view.fragments.allContacts;

import android.content.Intent;
import android.support.annotation.NonNull;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.api.services.people.v1.model.Person;

import java.util.ArrayList;
import java.util.List;

public interface AllContactsContract {
     interface IAllContactsView{
        void setContactList(List<Person> contacts);

        void showToast(@NonNull String message);



        void sendMessage(String number);

        void makeCall(String number);

        void onContactClicked(Person person);

        void setProgressBarVisible();


     }

     interface IAllContactsPresenter{

        void detachView();


        void sendMessage(String number);

        void makeCall(String number);

        void onRequestPermissionsResult(int requestCode,
                                        String permissions[], int[] grantResults,String number);

        boolean checkAndRequestPermissions(int permissionCode);

        void onContactClicked(Person person);

    }
}
