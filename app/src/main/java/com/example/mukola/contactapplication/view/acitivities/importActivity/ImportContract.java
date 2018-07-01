package com.example.mukola.contactapplication.view.acitivities.importActivity;

import android.content.Intent;
import android.support.annotation.NonNull;

import com.example.mukola.contactapplication.model.models.Contact;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.api.services.people.v1.model.Person;

import java.util.List;

public interface ImportContract {
    public interface IImportView{
        void showToast(@NonNull String message);

        void InitGoogleSignIn();

        void getIdToken();

        void setContactList(List<Person> contacts);

        void setProgressBarVisible();

        void openContact(@NonNull Contact contact);


    }

    public interface IImportPresenter{
        void detachView();

        void InitGoogleSignIn();

        void getIdToken();

        void setmGoogleApiClient(@NonNull GoogleApiClient mGoogleApiClient);

        void connectmGoogleApiClient();

        GoogleApiClient getmGoogleApiClient();

        void verification(Intent data);

        Contact personToContact(@NonNull Person person,@NonNull int userId,@NonNull String type);

    }
}
