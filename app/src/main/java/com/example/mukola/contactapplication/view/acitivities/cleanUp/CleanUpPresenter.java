package com.example.mukola.contactapplication.view.acitivities.cleanUp;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.mukola.contactapplication.R;
import com.example.mukola.contactapplication.model.database.PhotoSaver;
import com.example.mukola.contactapplication.model.models.Contact;
import com.example.mukola.contactapplication.model.peopleHelper.PeopleHelper;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.api.services.people.v1.People;
import com.google.api.services.people.v1.model.ListConnectionsResponse;
import com.google.api.services.people.v1.model.Person;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class CleanUpPresenter implements CleanUpContract.ICleanUpPresenter {

    public static final int REQUEST_CONTACT_PERMISSIONS = 1;


    @NonNull
    private CleanUpContract.ICleanUpView view;

    @NonNull
    private GoogleApiClient mGoogleApiClient;

    @NonNull
    private Activity activity;

    @NonNull
    private Context context;

    @NonNull
    private PhotoSaver photoSaver;

    public CleanUpPresenter(@NonNull CleanUpContract.ICleanUpView view, @NonNull Activity activity, @NonNull Context context){
        this.view = view;
        this.activity = activity;
        photoSaver = new PhotoSaver(context);
        this.context = context;
    }

    @Override
    public GoogleApiClient getmGoogleApiClient() {
        return mGoogleApiClient;
    }

    @Override
    public void openArchive() {
        view.openArchive();
    }

    @Override
    public void openContact(@NonNull Contact contact) {
        view.openContact(contact);
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
    public void detachView() {
        view = null;
    }

    @Override
    public void openTinder() {
        view.openTinder();
    }

    @Override
    public void openList() {
        view.openList();
    }

    @Override
    public   boolean checkAndRequestPermissions(int permissionCode) {

        if (permissionCode == REQUEST_CONTACT_PERMISSIONS){
            int callpermission = ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_CONTACTS);
            List<String> listPermissionsNeeded = new ArrayList<>();

            if (callpermission != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.READ_CONTACTS);
                ActivityCompat.requestPermissions(activity,
                        listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_CONTACT_PERMISSIONS);
                return false;
            }

        }

        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults){
        Log.d("TAG", "Permission callback called-------");
        switch (requestCode) {
            case REQUEST_CONTACT_PERMISSIONS:
                Log.d("TAG", "Permission CALL called-------");

                Map<String, Integer> perms = new HashMap<>();
                perms.put(Manifest.permission.READ_CONTACTS, PackageManager.PERMISSION_GRANTED);

                if (grantResults.length > 0) {
                    perms.put(permissions[0], grantResults[0]);

                    if (perms.get(Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {

                        view.importPhone();

                    } else {

                        Log.d("TAG", "Some permissions are not granted ask again ");

                        if (ActivityCompat.shouldShowRequestPermissionRationale(activity,Manifest.permission.CALL_PHONE)
                                ) {
                            showDialog(REQUEST_CONTACT_PERMISSIONS);
                        }
                        else {
                            explain("You need to give some mandatory permissions to continue. Do you want to go to app settings?");
                        }
                    }
                }
                break;


        }

    }

    private void showDialog(final int requestCode){
        showDialogOK("Service Permissions are required for this app",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                checkAndRequestPermissions(requestCode);
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                // proceed with logic by disabling the related features or quit the app.
                                activity.finish();
                                break;
                        }

                    }
                });
    }


    private void showDialogOK(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(activity)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", okListener)
                .create()
                .show();
    }
    private void explain(String msg){
        final android.support.v7.app.AlertDialog.Builder dialog = new android.support.v7.app.AlertDialog.Builder(activity);
        dialog.setMessage(msg)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        //  permissionsclass.requestPermission(type,code);
                        activity.startActivity(new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("com.example.mukola.contactapplication")));
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        activity.finish();
                    }
                });
        dialog.show();
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

        final List<Person> finalContactList1 = contactList;
        activity.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                final List<Contact> finalContactList = new ArrayList<>();

                for (Person p: finalContactList1) {
                    finalContactList.add(personToContact(p));
                }
                view.setProgressBarGone();

                Collections.sort(finalContactList, new Comparator<Contact>() {
                    @Override
                    public int compare(Contact contact2, Contact contact1)
                    {

                        return  contact2.getName().compareTo(contact1.getName());
                    }
                });

                view.setContactList(finalContactList);
            }
        });

    }

    @Override
    public void readContacts(){
        Cursor phones = activity.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null,null, null);
        final ArrayList<Contact> list = new ArrayList<>();
        while (phones.moveToNext())
        {
            Contact contact = new Contact();
            contact.setName(phones.getString(phones.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)));
            String number = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            contact.setNumber(number);
            contact.setEmail(context.getString(R.string.no_email));
            contact.setAddress(context.getString(R.string.no_address));
            contact.setCompany(phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Organization.COMPANY)));
            int id =  phones.getInt(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_ID));

            Bitmap bitmap = queryContactImage(id);

            if (bitmap!=null) {
                contact.setPhotoUrl(photoSaver.saveToInternalStorage(bitmap));
            }else {
                contact.setPhotoUrl("null");
            }
            contact.setFavorite(false);
            contact.setBlacklistId(number);
            list.add(contact);

        }
        phones.close();

        activity.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                view.setProgressBarGone();
                view.setContactList(list);
            }
        });
    }

    private Contact personToContact(@NonNull Person person) {

        final Contact contact = new Contact();

        contact.setBlacklistId(person.getResourceName());


        if (person.getNames() != null) {
            String nm = person.getNames().get(0).getDisplayName();
            if (nm != null) {
                contact.setName(nm);
            } else {
                contact.setName(context.getString(R.string.no_name));
            }
        }else {
            contact.setName(context.getString(R.string.no_name));
        }

        if (person.getPhoneNumbers() != null) {
            String ph = person.getPhoneNumbers().get(0).getCanonicalForm();
            if (ph != null) {
                contact.setNumber(ph);
            } else {
                contact.setNumber(context.getString(R.string.no_phone));
            }
        }else {
            contact.setNumber(context.getString(R.string.no_phone));
        }

        if (person.getEmailAddresses() != null) {
            String em = person.getEmailAddresses().get(0).getValue();
            if (em != null) {
                contact.setEmail(em);
            } else {
                contact.setEmail(context.getString(R.string.no_email));
            }
        }else {
            contact.setEmail(context.getString(R.string.no_email));
        }

        if (person.getAddresses() != null) {
            String ad = person.getAddresses().get(0).getCity();
            if (ad != null) {
                contact.setAddress(ad);
            } else {
                contact.setAddress(context.getString(R.string.no_address));
            }
        }else {
            contact.setAddress(context.getString(R.string.no_address));
        }

        if (person.getOrganizations() != null) {
            String cm = person.getOrganizations().get(0).getName();
            if (cm != null) {
                contact.setCompany(cm);
            } else {
                contact.setCompany(context.getString(R.string.no_company));
            }
        }else {
            contact.setCompany(context.getString(R.string.no_company));
        }

        contact.setFavorite(false);

        if (person.getPhotos() != null) {

            Glide.with(context)
                    .asBitmap()
                    .load(person.getPhotos().get(0).getUrl())
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                            contact.setPhotoUrl(photoSaver.saveToInternalStorage(resource));
                        }
                    });

        }

        return contact;
    }


    private Bitmap queryContactImage(int imageDataRow) {
        Cursor c = context.getContentResolver().query(ContactsContract.Data.CONTENT_URI, new String[] {
                ContactsContract.CommonDataKinds.Photo.PHOTO
        }, ContactsContract.Data._ID + "=?", new String[] {
                Integer.toString(imageDataRow)
        }, null);
        byte[] imageBytes = null;
        if (c != null) {
            if (c.moveToFirst()) {
                imageBytes = c.getBlob(0);
            }
            c.close();
        }

        if (imageBytes != null) {
            return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        } else {
            return null;
        }
    }


}
