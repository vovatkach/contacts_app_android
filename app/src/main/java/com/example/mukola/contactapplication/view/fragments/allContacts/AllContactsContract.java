package com.example.mukola.contactapplication.view.fragments.allContacts;

import android.support.annotation.NonNull;

import com.example.mukola.contactapplication.model.models.Contact;

import java.util.ArrayList;
import java.util.List;

public interface AllContactsContract {
     interface IAllContactsView{
        void  setContactList(List<Contact> contacts);

        void showToast(@NonNull String message);

        void openContact(@NonNull Contact contact);

        void setImportButtonVisible();

        void setBlacklist(ArrayList<String> list);

     }

     interface IAllContactsPresenter{

        void detachView();

         void addToArchive(@NonNull int userId,@NonNull Contact contact);

         void addToContact(@NonNull int userId,@NonNull Contact contact);

         void openContact(@NonNull Contact contact);

         void getBlacklist(@NonNull int userId);

         void addToBlackList(@NonNull int userId,@NonNull String contactId);

    }
}
