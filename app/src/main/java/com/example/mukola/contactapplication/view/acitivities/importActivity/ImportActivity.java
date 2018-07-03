package com.example.mukola.contactapplication.view.acitivities.importActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mukola.contactapplication.R;
import com.example.mukola.contactapplication.model.models.Contact;
import com.example.mukola.contactapplication.model.models.User;
import com.example.mukola.contactapplication.model.peopleHelper.PeopleHelper;
import com.example.mukola.contactapplication.view.acitivities.adapter.ContactListAdapter;
import com.example.mukola.contactapplication.view.acitivities.adapter.ImportContactsListAdapter;
import com.example.mukola.contactapplication.view.acitivities.contact.ContactActivity;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.api.services.people.v1.PeopleScopes;
import com.google.api.services.people.v1.model.Person;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ImportActivity extends AppCompatActivity implements ImportContract.IImportView,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks,ImportContactsListAdapter.OnItemClicked


{

    @BindView(R.id.rv_contacts_is)
    RecyclerView list;

    @BindView(R.id.progressBar_is)
    ProgressBar progressBar;

    @BindView(R.id.tv_no_contact_is)
    TextView tv;

    @OnClick(R.id.import_back)
    void onBackClick(View view) {
        onBackPressed();
    }

    @NonNull
    private ImportContract.IImportPresenter presenter;

    @NonNull
    private final int RC_INTENT = 200;

    @NonNull
    private final int RC_API_CHECK = 100;

    @NonNull
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_is);
        setSupportActionBar(toolbar);
        toolbar.setSubtitleTextColor(Color.WHITE);

        setTitle(getString(R.string.importT));

        ButterKnife.bind(this);

        presenter = new ImportPresenter(this,this,this);

        getData();

        presenter.InitGoogleSignIn();

        presenter.getIdToken();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("ON ACTIVITULT","AAAAAA");

        switch (requestCode) {
            case RC_INTENT:
                presenter.verification(data);
                break;
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d("connection", "msg: " + connectionResult.getErrorMessage());

        GoogleApiAvailability mGoogleApiAvailability = GoogleApiAvailability.getInstance();
        Dialog dialog = mGoogleApiAvailability.getErrorDialog(this, connectionResult.getErrorCode(), RC_API_CHECK);
        dialog.show();
    }

    @Override
    public void InitGoogleSignIn() {
        Log.d("INIT GOOGLE","SIGNIN");

        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                // The serverClientId is an OAuth 2.0 web client ID
                .requestServerAuthCode(PeopleHelper.CLIENT_ID)
                .requestEmail()
                .requestScopes(new Scope(Scopes.PLUS_LOGIN),
                        new Scope(PeopleScopes.CONTACTS_READONLY),
                        new Scope(PeopleScopes.USER_EMAILS_READ),
                        new Scope(PeopleScopes.USERINFO_EMAIL),
                        new Scope(PeopleScopes.USER_PHONENUMBERS_READ))
                .build();

        // To connect with Google Play Services and Sign In
        presenter.setmGoogleApiClient(new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addOnConnectionFailedListener(this)
                .addConnectionCallbacks(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, signInOptions)
                .build());

        presenter.connectmGoogleApiClient();

    }

    @Override
    public void getIdToken() {
        Log.d("GET ID","TOKEN");

        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(presenter.getmGoogleApiClient());
        startActivityForResult(signInIntent, RC_INTENT);

    }

    @Override
    public void setContactList(List<Person> contacts) {

        if (contacts.size() == 0) {

           list.setVisibility(View.GONE);
           tv.setVisibility(View.VISIBLE);

        } else {
            list.setVisibility(View.VISIBLE);
            list.setLayoutManager(new LinearLayoutManager(this));

            LinearLayoutManager lm = new LinearLayoutManager(this);
            lm.setOrientation(LinearLayoutManager.VERTICAL);
            list.setLayoutManager(lm);

            ArrayList<Person> l = new ArrayList<>(contacts);

            ImportContactsListAdapter mAdapter = new ImportContactsListAdapter(l, this);
            // set adapter
            mAdapter.setOnClick(this);

            progressBar.setVisibility(View.GONE);

            list.setAdapter(mAdapter);
            // set item animator to DefaultAnimator
            list.setItemAnimator(new DefaultItemAnimator());
        }
    }

    private void getData() {
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            user = (User) extras.getSerializable("user");
        }
    }

    @Override
    public void openContact(@NonNull Contact contact) {
        Intent intent = new Intent(this, ContactActivity.class);
        intent.putExtra("user",user);
        intent.putExtra("contact",contact);
        startActivity(intent);
    }

    @Override
    public void setProgressBarVisible() {
        progressBar.setVisibility(View.VISIBLE);
    }


    @Override
    public void showToast(@NonNull String message) {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAddClick(@NonNull Person person) {
        presenter.personToContact(person,user.getId(),"add");
    }

    @Override
    public void onUserClick(@NonNull Person person) {
        presenter.personToContact(person,user.getId(),"open");
    }
}
