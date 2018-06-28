package com.example.mukola.contactapplication.view.acitivities.mainScreen;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.example.mukola.contactapplication.model.models.User;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.api.services.people.v1.model.Person;

import java.util.List;

public interface MSContract {
    public interface IMainScreenView{
        void showToast(@NonNull String message);

        void InitGoogleSignIn();

        void getIdToken();

        void setContactList(List<Person> contacts);

        void setProgressBarVisible();

        void sendMessage(String number);

        void makeCall(String number);


    }

    public interface IMainScreenPresenter{
        void InitGoogleSignIn();

        void detachView();

        void getIdToken();

        void verification(Intent data);

        GoogleApiClient getmGoogleApiClient();

        void setmGoogleApiClient(@NonNull GoogleApiClient mGoogleApiClient);

        void connectmGoogleApiClient();

        void sendMessage(String number);

        void makeCall(String number);

        void onRequestPermissionsResult(int requestCode,
                                        String permissions[], int[] grantResults,String number);

        boolean checkAndRequestPermissions(int permissionCode);


    }
}
