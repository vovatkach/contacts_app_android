package com.example.mukola.contactapplication.view.acitivities.cityReminder;

import android.support.annotation.NonNull;

import com.example.mukola.contactapplication.model.models.Contact;

import java.util.ArrayList;
import java.util.List;

public interface ReminderContract {
    public interface IContactView{

        void showToast(@NonNull String message);

        void showAlert();

        void setContactList(@NonNull ArrayList<Contact> contacts);

        void setCity(@NonNull String city);

        void setNoContacts();

        void setProgressBarVisible();

        void setProgressBarGone();

        void setSpinner(@NonNull ArrayList<String> list);

        void sendMessage(@NonNull String number);

        void makeCall(@NonNull String number);

        void onContactClicked(@NonNull Contact contact);

    }

    public interface IContactPresenter{

        void detachView();

        void getAllCities();

        void setGetContactsInCity(@NonNull String city);

        void onContactClicked(@NonNull Contact contact);

        void sendMessage(@NonNull String number);

        void makeCall(@NonNull String number);

        void onRequestPermissionsResult(int requestCode,
                                        String permissions[], int[] grantResults,String number);

        boolean checkAndRequestPermissions(int permissionCode);

        void addToFavorite(@NonNull int userId,@NonNull Contact contact);

        void deleteFRomFavorite(@NonNull int userId,@NonNull Contact contact);

        void getHeaderListLatter(@NonNull ArrayList<Contact> usersList, ArrayList<Contact> mSectionList);

    }
}
