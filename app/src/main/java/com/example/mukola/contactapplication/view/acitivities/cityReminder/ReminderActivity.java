package com.example.mukola.contactapplication.view.acitivities.cityReminder;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mukola.contactapplication.R;
import com.example.mukola.contactapplication.model.models.Contact;
import com.example.mukola.contactapplication.model.models.User;
import com.example.mukola.contactapplication.view.acitivities.adapter.ContactListAdapter;
import com.example.mukola.contactapplication.view.acitivities.contact.ContactActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ReminderActivity extends AppCompatActivity implements ReminderContract.IContactView,
                                    ContactListAdapter.OnItemClicked
{

    @BindView(R.id.rv_city_contact)
    RecyclerView list;


    @BindView(R.id.spinner_cities)
    Spinner cities;

    @BindView(R.id.et_current_city)
    EditText city;

    @BindView(R.id.tv_city_no_contacts)
    TextView tv;

    @BindView(R.id.progressBar_city)
    ProgressBar progressBar;

    @OnClick(R.id.city_back)
    void onBackClick(View view) {
        onBackPressed();
    }

    @NonNull
    private User user;

    @NonNull
    private ArrayList<String> cit;

    @NonNull
    private boolean isSpinnerInitial = false;

    @NonNull
    private String pNumber;

    @NonNull
    private ReminderContract.IContactPresenter presenter;

    private ArrayList<Contact> mSectionList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_city);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setSubtitleTextColor(Color.WHITE);

        setTitle(getString(R.string.city_reminder));

        getData();

        presenter = new ReminderPresenter(this,this,this,user.getId());

        mSectionList = new ArrayList<>();

        presenter.getAllCities();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    @Override
    public void showToast(@NonNull String message) {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

    public void showAlert() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Enable Location")
                .setMessage("Your Locations Settings is set to 'Off'.\nPlease Enable Location to " +
                        "use this app")
                .setPositiveButton("Location Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                    }
                });
        dialog.show();
    }

    @Override
    public void setContactList(ArrayList<Contact> contacts) {
        mSectionList.clear();

        list.setVisibility(View.VISIBLE);

        tv.setVisibility(View.GONE);

        presenter.getHeaderListLatter(contacts,mSectionList);

        LinearLayoutManager lm = new LinearLayoutManager(this);
        lm.setOrientation(LinearLayoutManager.VERTICAL);
        list.setLayoutManager(lm);


        ContactListAdapter mAdapter = new ContactListAdapter(mSectionList, this);
        // set adapter
        mAdapter.setOnClick(this);

        list.setAdapter(mAdapter);

        // set item animator to DefaultAnimator
        list.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public void setCity(@NonNull String city) {
        this.city.setText(city);
    }

    @Override
    public void setNoContacts() {
        list.setVisibility(View.GONE);
        tv.setText(getText(R.string.no_city_contact));
        tv.setVisibility(View.VISIBLE);
    }

    @Override
    public void setProgressBarVisible() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void setProgressBarGone() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void setSpinner(@NonNull ArrayList<String> list) {
        cit = list;
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item,
                        list);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout
                .simple_spinner_dropdown_item);
        cities.setAdapter(spinnerArrayAdapter);

        cities.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                if(isSpinnerInitial){ // globar var boolean isSpinnerInitial = false;
                    Log.d("ON ITEM CLICK",cit.get(position) );
                    presenter.setGetContactsInCity(cit.get(position));
                    setCity(cit.get(position));
                }else
                    isSpinnerInitial=true;

            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    private void getData() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            user = (User) extras.getSerializable("user");
        }
    }

    @Override
    public void makeCall(final String number) {
        pNumber = number;

        if (presenter.checkAndRequestPermissions(ReminderPresenter.REQUEST_ID_CALL_PERMISSIONS)) {
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:" + number));
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            startActivity(intent);
        }
    }

    @Override
    public void onContactClicked(@NonNull Contact contact) {
        Intent intent = new Intent(this, ContactActivity.class);
        intent.putExtra("user",user);
        intent.putExtra("contact",contact);
        startActivity(intent);
    }

    @Override
    public void sendMessage(final String number){
        pNumber = number;
        if(presenter.checkAndRequestPermissions(ReminderPresenter.REQUEST_ID_SMS_PERMISSIONS)) {

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("sms:" + number));
            startActivity(intent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        presenter.onRequestPermissionsResult(requestCode, permissions, grantResults,pNumber);
    }





    @Override
    public void onFavClick(@NonNull Contact contact) {
        if (contact.isFavorite()){
            presenter.deleteFRomFavorite(user.getId(),contact);
        }else {
            presenter.addToFavorite(user.getId(),contact);
        }
    }

    @Override
    public void onUserClick(@NonNull Contact contact) {
        presenter.onContactClicked(contact);
    }


}
