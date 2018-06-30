package com.example.mukola.contactapplication.view.acitivities.importActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.mukola.contactapplication.R;
import com.example.mukola.contactapplication.model.models.Contact;
import com.example.mukola.contactapplication.model.peopleHelper.PeopleHelper;
import com.example.mukola.contactapplication.model.repositories.AddToContactsRepository;
import com.example.mukola.contactapplication.model.repositories.AddToContactsRepositoryImpl;
import com.example.mukola.contactapplication.view.acitivities.mainScreen.MSContract;
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

public class ImportPresenter implements ImportContract.IImportPresenter {

    @NonNull
    private ImportContract.IImportView view;

    @NonNull
    GoogleApiClient mGoogleApiClient;

    @NonNull
    Activity activity;

    @NonNull
    AddToContactsRepository addToContactsRepository;


    public ImportPresenter(@NonNull ImportContract.IImportView view, @NonNull Activity activity,@NonNull Context context){
        this.view = view;
        this.activity = activity;
        addToContactsRepository = new AddToContactsRepositoryImpl(context);
    }

    @Override
    public void detachView() {
        view = null;
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

            view.setProgressBarVisible();

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

    @Override
    public Contact personToContact(@NonNull Person person) {

        Contact contact = new Contact();

        if (person.getNames() != null) {
            String nm = person.getNames().get(0).getDisplayName();
            if (nm != null) {
                contact.setName(nm);
            } else {
                contact.setName(activity.getString(R.string.no_name));
            }
        }else {
            contact.setName(activity.getString(R.string.no_name));
        }

        if (person.getPhoneNumbers() != null) {
            String ph = person.getPhoneNumbers().get(0).getCanonicalForm();
            if (ph != null) {
                contact.setNumber(ph);
            } else {
                contact.setNumber(activity.getString(R.string.no_phone));
            }
        }else {
            contact.setNumber(activity.getString(R.string.no_phone));
        }

        if (person.getEmailAddresses() != null) {
            String em = person.getEmailAddresses().get(0).getValue();
            if (em != null) {
                contact.setEmail(em);
            } else {
                contact.setEmail(activity.getString(R.string.no_email));
            }
        }else {
            contact.setEmail(activity.getString(R.string.no_email));
        }

        if (person.getAddresses() != null) {
            String ad = person.getAddresses().get(0).getCity();
            if (ad != null) {
                contact.setAddress(ad);
            } else {
                contact.setAddress(activity.getString(R.string.no_address));
            }
        }else {
            contact.setAddress(activity.getString(R.string.no_address));
        }

        if (person.getOrganizations() != null) {
            String cm = person.getOrganizations().get(0).getName();
            if (cm != null) {
                contact.setCompany(cm);
            } else {
                contact.setCompany(activity.getString(R.string.no_company));
            }
        }else {
            contact.setCompany(activity.getString(R.string.no_company));
        }

        if (person.getPhotos() != null) {
            contact.setPhotoUrl(person.getPhotos().get(0).getUrl());
        }

        contact.setFavorite(false);

       return contact;
    }

    @Override
    public void openContact(@NonNull Contact contact) {
        view.openContact(contact);
    }

    @Override
    public void addToContact(@NonNull int userId, @NonNull Contact contact){
        addToContactsRepository.addToContacts(userId, contact, new AddToContactsRepository.addToContactsCallback() {
            @Override
            public void addedSuccessfull() {
                view.showToast(activity.getString(R.string.addes_successfull));
            }

            @Override
            public void notSuccessfull() {
                view.showToast(activity.getString(R.string.error));
            }
        });
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
                view.setContactList(finalContactList);
            }
        });

    }


}
