package com.example.mukola.contactapplication.view.fragments.allContacts;

import android.support.annotation.NonNull;

import com.example.mukola.contactapplication.model.models.Contact;

import java.util.List;

public interface AllContactsContract {
     interface IAllContactsView{
        void setContactList(List<Contact> contacts);

        void showToast(@NonNull String message);



        void sendMessage(@NonNull String number);

        void makeCall(@NonNull String number);

        void onContactClicked(@NonNull Contact contact);

        void setImportButtonVisible();

     }

     interface IAllContactsPresenter{

        void detachView();


        void sendMessage(@NonNull String number);

        void makeCall(@NonNull String number);

        void onRequestPermissionsResult(int requestCode,
                                        String permissions[], int[] grantResults,String number);

        boolean checkAndRequestPermissions(int permissionCode);

        void onContactClicked(@NonNull Contact contact);

         void getContacts(@NonNull int userId);

    }
}
