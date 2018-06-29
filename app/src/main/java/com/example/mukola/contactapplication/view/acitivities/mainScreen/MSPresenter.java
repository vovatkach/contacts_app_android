package com.example.mukola.contactapplication.view.acitivities.mainScreen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.mukola.contactapplication.model.peopleHelper.PeopleHelper;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.api.services.people.v1.People;
import com.google.api.services.people.v1.model.ListConnectionsResponse;
import com.google.api.services.people.v1.model.Person;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class MSPresenter implements MSContract.IMainScreenPresenter{

    @NonNull
    private MSContract.IMainScreenView view;

    @NonNull
    GoogleApiClient mGoogleApiClient;

    @NonNull
    Activity activity;




    public MSPresenter(@NonNull MSContract.IMainScreenView view,@NonNull Activity activity){
        this.view = view;
        this.activity = activity;
    }

    @Override
    public void detachView() {
        view = null;
    }

    @Override
    public void openAllContacts() {
        view.openAllContacts();
    }

    @Override
    public void openContact(Bundle person, int userId) {
        view.openContact(person,userId);
    }

    @Override
    public void openFavorite( int userId) {
        view.openFavorite(userId);
    }




    @Override
    public GoogleApiClient getmGoogleApiClient() {
        return mGoogleApiClient;
    }

    @Override
    public void setmGoogleApiClient(@NonNull GoogleApiClient mGoogleApiClient) {
        this.mGoogleApiClient = mGoogleApiClient;
    }

    @Override
    public void connectmGoogleApiClient(){
        mGoogleApiClient.connect();
    }


    @Override
    public void InitGoogleSignIn() {
        view.InitGoogleSignIn();
    }

    @Override
    public void getIdToken() {
        view.getIdToken();
    }

    @Override
    public void verification(Intent data) {
        Log.d(TAG, "sign in result");
        GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

        if (result.isSuccess()) {
            final GoogleSignInAccount acct = result.getSignInAccount();

            Log.d(TAG, "onActivityResult:GET_TOKEN:success:" + result.getStatus().isSuccess());
            Log.d(TAG, "auth Code:" + acct.getServerAuthCode());

            new Thread(new Runnable() {
                @Override
                public void run() {
                    retrieveContacts(acct.getServerAuthCode());
                }
            }).start();


        } else {

            Log.d(TAG, result.getStatus().toString() + "\nmsg: " + result.getStatus().getStatusMessage());
        }
    }

    private void retrieveContacts(String authCode) {
        List<Person> contactList = new ArrayList<>();

        try {
            People peopleService = PeopleHelper.setUp(activity, authCode);

            ListConnectionsResponse response = peopleService.people().connections()
                    .list("people/me")
                    .setRequestMaskIncludeField("person.names,person.emailAddresses,person.phoneNumbers,person.photos")
                    .execute();

            List<Person> connections = response.getConnections();
            if (connections != null) {
                contactList = connections;
            }else{
                Log.d("AAAAAAAA", "NULLLL");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        final List<Person> finalContactList = contactList;

        activity.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                view.initViewPager((ArrayList<Person>) finalContactList);
            }
        });

    }


}
