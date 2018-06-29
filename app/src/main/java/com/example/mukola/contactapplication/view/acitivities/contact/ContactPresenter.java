package com.example.mukola.contactapplication.view.acitivities.contact;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.ContentFrameLayout;
import android.util.Log;

import com.example.mukola.contactapplication.model.repositories.AddToFavoritesRepository;
import com.example.mukola.contactapplication.model.repositories.AddToFavoritesRepositoryImpl;
import com.example.mukola.contactapplication.model.repositories.DeleteFromFavoritesRepository;
import com.example.mukola.contactapplication.model.repositories.DeleteFromFavoritesRepositoryImpl;
import com.example.mukola.contactapplication.model.repositories.GetFavoritesRepository;
import com.example.mukola.contactapplication.model.repositories.GetFavoritesRepositoryImpl;
import com.google.api.services.people.v1.model.Person;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContactPresenter implements ContactContract.IContactPresenter{

    public static final int REQUEST_ID_CALL_PERMISSIONS = 1;

    public static final int REQUEST_ID_SMS_PERMISSIONS = 2;

    @NonNull
    private ContactContract.IContactView view;

    @NonNull
    private Activity activity;

    @NonNull
    private AddToFavoritesRepository addToFavoritesRepository;

    @NonNull
    private GetFavoritesRepository getFavoritesRepository;

    @NonNull
    private DeleteFromFavoritesRepository deleteFromFavoritesRepository;

    private ArrayList<String> favorites;


    public ContactPresenter (@NonNull ContactContract.IContactView view,@NonNull Activity activity,
                             @NonNull Context context){
        this.view = view;
        this.activity = activity;
        addToFavoritesRepository = new AddToFavoritesRepositoryImpl(context);
        getFavoritesRepository = new GetFavoritesRepositoryImpl(context);
        deleteFromFavoritesRepository = new DeleteFromFavoritesRepositoryImpl(context);
    }



    @Override
    public void detachView() {
        view = null;
    }

    @Override
    public void sendMessage(String number) {
        view.sendMessage(number);
    }

    @Override
    public void makeCall(String number) {
        view.makeCall(number);
    }

    @Override
    public   boolean checkAndRequestPermissions(int permissionCode) {

        if (permissionCode == REQUEST_ID_CALL_PERMISSIONS){
            int callpermission = ContextCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE);
            List<String> listPermissionsNeeded = new ArrayList<>();

            if (callpermission != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.CALL_PHONE);
                ActivityCompat.requestPermissions(activity, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_CALL_PERMISSIONS);
                return false;
            }

        }else if(permissionCode == REQUEST_ID_SMS_PERMISSIONS){

            int smspermission = ContextCompat.checkSelfPermission(activity, Manifest.permission.SEND_SMS);

            List<String> listPermissionsNeeded = new ArrayList<>();

            if (smspermission != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.SEND_SMS);
                ActivityCompat.requestPermissions(activity,listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_SMS_PERMISSIONS);
                return false;
            }
        }

        return true;
    }

    @Override
    public void addToFavorites(int userId, String contactId) {
        addToFavoritesRepository.addToFavorites(userId, contactId, new AddToFavoritesRepository.addToFavoritesCallback() {
            @Override
            public void addedSuccessfull() {
                view.showToast("Contact added to Favorites.");
            }

            @Override
            public void notSuccessfull() {
                view.showToast("Error! Please try again.");

            }
        });
    }

    @Override
    public void getFavorites(int userId) {
        getFavoritesRepository.getFavorites(userId, new GetFavoritesRepository.GetFavoritesCallback() {
            @Override
            public void onFavoritesGet(@NonNull ArrayList<String> list) {
                favorites = list;
            }

            @Override
            public void notFound() {
                favorites = null;
            }
        });
    }

    @Override
    public boolean checkIsFavorite(String contactId) {
        boolean b = false;

        if (favorites!=null) {
            for (String s : favorites) {
                if (s.equals(contactId)) {
                    b = true;
                }
            }
        }

        return b;
    }

    @Override
    public void deleteFromFavorites(int userId, String contactId) {
        deleteFromFavoritesRepository.deleteFromFavorites(userId, contactId, new DeleteFromFavoritesRepository.deleteFromFavoritesCallback() {
            @Override
            public void deletedSuccessfull() {
                view.showToast("Contact is successfull removed from favorites.");
            }

            @Override
            public void notSuccessfull() {
                view.showToast("Error! Please try again.");
            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults,String number){
        Log.d("TAG", "Permission callback called-------");
        switch (requestCode) {
            case REQUEST_ID_CALL_PERMISSIONS:
                Log.d("TAG", "Permission CALL called-------");

                Map<String, Integer> perms = new HashMap<>();
                perms.put(Manifest.permission.CALL_PHONE, PackageManager.PERMISSION_GRANTED);

                if (grantResults.length > 0) {
                    perms.put(permissions[0], grantResults[0]);

                    if (perms.get(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {

                        Log.d("TAG", "call phone permission granted");
                        view.makeCall(number);

                    } else {

                        Log.d("TAG", "Some permissions are not granted ask again ");

                        if (ActivityCompat.shouldShowRequestPermissionRationale(activity,Manifest.permission.CALL_PHONE)
                                ) {
                            showDialog(REQUEST_ID_CALL_PERMISSIONS);
                        }
                        else {
                            explain("You need to give some mandatory permissions to continue. Do you want to go to app settings?");
                        }
                    }
                }
                break;

            case REQUEST_ID_SMS_PERMISSIONS:
                Log.d("TAG", "Permission SMS called-------");

                Map<String, Integer> perm = new HashMap<>();
                perm.put(Manifest.permission.SEND_SMS, PackageManager.PERMISSION_GRANTED);

                if (grantResults.length > 0) {
                    perm.put(permissions[0], grantResults[0]);

                    if (perm.get(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {

                        Log.d("TAG", "sms permissions granted");
                        view.sendMessage(number);

                    } else {

                        Log.d("TAG", "Some permissions are not granted ask again ");

                        if (ActivityCompat.shouldShowRequestPermissionRationale(activity,Manifest.permission.SEND_SMS)
                                ) {
                            showDialog(REQUEST_ID_SMS_PERMISSIONS);
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
}
