package com.example.mukola.contactapplication.view.acitivities.cleanUp;

import android.content.Intent;
import android.support.annotation.NonNull;

import com.example.mukola.contactapplication.model.models.Contact;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.List;

public class CleanUpContract {
    public interface ICleanUpView{

        void showToast(@NonNull String message);

        void openTinder();

        void openList();

        void InitGoogleSignIn();

        void getIdToken();

        void setContactList(List<Contact> contacts);


        void openArchive();

        void openContact(@NonNull Contact contact);

        void importPhone();

        void setProgressBarVisible();

        void setProgressBarGone();


    }

    public interface ICleanUpPresenter{

        void detachView();

        void openTinder();

        void openList();

        void verification(Intent data);

        void getIdToken();

        void InitGoogleSignIn();

        void setmGoogleApiClient(@NonNull GoogleApiClient mGoogleApiClient);

        void connectmGoogleApiClient();

        GoogleApiClient getmGoogleApiClient();

        void openArchive();

        void openContact(@NonNull Contact contact);

        void readContacts();

        boolean checkAndRequestPermissions(int permissionCode);

        void onRequestPermissionsResult(int requestCode,
                                        String permissions[], int[] grantResults);

    }
}
