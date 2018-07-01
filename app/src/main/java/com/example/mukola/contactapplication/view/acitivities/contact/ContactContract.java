package com.example.mukola.contactapplication.view.acitivities.contact;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import com.example.mukola.contactapplication.model.models.Contact;

public interface ContactContract {
    public interface IContactView{
        void sendMessage(@NonNull String number);

        void makeCall(@NonNull String number);

        void showToast(@NonNull String message);

        void setPhoto(@NonNull Bitmap bitmap);
    }

    public interface IContactPresenter{

        void detachView();

        void sendMessage(@NonNull String number);

        void makeCall(@NonNull String number);

        void onRequestPermissionsResult(int requestCode,
                                        String permissions[], int[] grantResults,String number);

        boolean checkAndRequestPermissions(int permissionCode);

        void addToFavorites(@NonNull int userId,@NonNull int contactId);

        void deleteFromFavorites(@NonNull int userId,@NonNull int contactId);

        void editContact(@NonNull int usedId,@NonNull Contact contact);

        void deleteContact(@NonNull int userId,@NonNull int contactId);

        void getPhoto(@NonNull String path);

    }
}
