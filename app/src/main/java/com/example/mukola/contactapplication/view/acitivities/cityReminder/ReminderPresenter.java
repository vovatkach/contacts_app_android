package com.example.mukola.contactapplication.view.acitivities.cityReminder;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.example.mukola.contactapplication.R;
import com.example.mukola.contactapplication.model.models.Contact;
import com.example.mukola.contactapplication.model.repositories.GetAllCitiesRepository;
import com.example.mukola.contactapplication.model.repositories.GetAllCitiesRepositoryImpl;
import com.example.mukola.contactapplication.model.repositories.GetContactsInCityRepository;
import com.example.mukola.contactapplication.model.repositories.GetContactsInCityRepositoryImpl;
import com.example.mukola.contactapplication.model.repositories.GetContactsRepository;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static android.support.constraint.Constraints.TAG;

public class ReminderPresenter implements ReminderContract.IContactPresenter,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener {

    private long UPDATE_INTERVAL = 500 * 1000;

    private long FASTEST_INTERVAL = 500  * 1000;

    public static final int REQUEST_ID_CALL_PERMISSIONS = 1;

    public static final int REQUEST_ID_SMS_PERMISSIONS = 2;

    @NonNull
    private ReminderContract.IContactView view;

    @NonNull
    private GetContactsInCityRepository getContactsInCityRepository;

    @NonNull
    private GetAllCitiesRepository getAllCitiesRepository;

    @NonNull
    private  Context context;

    @NonNull
    private Activity activity;

    @NonNull
    private int userId;

    @NonNull
    private GoogleApiClient mGoogleApiClient;

    @NonNull
    private Location mLocation;

    @NonNull
    private LocationManager mLocationManager;

    @NonNull
    private LocationRequest mLocationRequest;

    @NonNull
    private LocationManager locationManager;


    public ReminderPresenter(@NonNull ReminderContract.IContactView view,
                              @NonNull Context context,@NonNull Activity activity,@NonNull int userId){
        this.view = view;
        getContactsInCityRepository = new GetContactsInCityRepositoryImpl(context);
        getAllCitiesRepository = new GetAllCitiesRepositoryImpl(context);
        this.context = context;
        this.activity = activity;
        this.userId = userId;
        initModules();

    }

    private void initModules(){
        view.setProgressBarVisible();
        checkLocation();
        initGoogleApi();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    private void initGoogleApi(){
        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mLocationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
    }

    @Override
    public void detachView() {
        view = null;
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void getAllCities() {
        getAllCitiesRepository.getCities(userId, new GetAllCitiesRepository.GetCitiesCallback() {
            @Override
            public void onCitiesGet(@NonNull ArrayList<String> list) {
                ArrayList<String> list1 = new ArrayList<>();
                for (String s:list) {
                    if (!s.equals(activity.getString(R.string.no_address))){
                        list1.add(s);
                    }
                }
                view.setSpinner(list1);
            }

            @Override
            public void notFound() {
                ArrayList<String> list = new ArrayList<>();
                list.add("No cities");
                view.setSpinner(list);
            }
        });
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        startLocationUpdates();

        mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if(mLocation == null){
            startLocationUpdates();
        }
        if (mLocation == null) {
           view.showToast("Location not Detected");
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Connection Suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i(TAG, "Connection failed. Error: " + connectionResult.getErrorCode());
    }

    @Override
    public void onLocationChanged(final Location location) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                geoCoding(location);
            }
        }).start();

    }

    protected void startLocationUpdates() {
        // Create the location request
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL)
                .setFastestInterval(FASTEST_INTERVAL);
        // Request location updates
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                mLocationRequest, this);
        Log.d("reque", "--->>>>");
    }

    private boolean checkLocation() {
        if(!isLocationEnabled())
            view.showAlert();
        return isLocationEnabled();
    }



    private boolean isLocationEnabled() {
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    private void geoCoding(Location location){
        Geocoder gc = new Geocoder(context);
        if(gc.isPresent()){
            List<Address> list = null;
            Log.d("ALLAH",String.valueOf(location.getLatitude()));
            Log.d("AKBAR",String.valueOf(location.getLongitude()));
            try {
                list = gc.getFromLocation(location.getLatitude(), location.getLongitude(),1);
                Address address = list.get(0);
                StringBuffer str = new StringBuffer();
                str.append(address.getAdminArea().replaceAll("[^a-zA-Z0-9]", ""));
                final String strAddress = str.toString();
                activity.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Log.d("CITY",strAddress);
                        view.setCity(strAddress);
                        setGetContactsInCity(strAddress);
                    }
                });

            } catch (IOException e) {
                e.printStackTrace();
                activity.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Log.d("EXCEPTION","GEO");
                    }
                });
            }

        }
    }

    public void setGetContactsInCity(@NonNull String city){
        getContactsInCityRepository.getContacts(userId, city, new GetContactsInCityRepository.GetContactsCallback() {
            @Override
            public void onContactsGet(@NonNull ArrayList<Contact> list) {
                view.setProgressBarGone();
                view.setContactList(list);
            }

            @Override
            public void notFound() {
                view.setProgressBarGone();
                view.setNoContacts();
            }
        });
    }

    @Override
    public void sendMessage(@NonNull String number) {
        view.sendMessage(number);
    }

    @Override
    public void makeCall(@NonNull String number) {
        view.makeCall(number);
    }



    @Override
    public  boolean checkAndRequestPermissions(int permissionCode) {

        if (permissionCode == REQUEST_ID_CALL_PERMISSIONS){
            int callpermission = ContextCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE);
            List<String> listPermissionsNeeded = new ArrayList<>();

            if (callpermission != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.CALL_PHONE);
                ActivityCompat.requestPermissions(activity,
                        listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_CALL_PERMISSIONS);
                return false;
            }

        }else if(permissionCode == REQUEST_ID_SMS_PERMISSIONS){

            int smspermission = ContextCompat.checkSelfPermission(activity, Manifest.permission.SEND_SMS);

            List<String> listPermissionsNeeded = new ArrayList<>();

            if (smspermission != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.SEND_SMS);
                ActivityCompat.requestPermissions(activity,
                        listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_SMS_PERMISSIONS);
                return false;
            }
        }

        return true;
    }

    @Override
    public void onContactClicked(@NonNull Contact contact) {
        view.onContactClicked(contact);
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
