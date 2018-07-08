package com.example.mukola.contactapplication.view.acitivities.cleanUp;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;

import com.example.mukola.contactapplication.R;
import com.example.mukola.contactapplication.model.models.Contact;
import com.example.mukola.contactapplication.model.models.User;
import com.example.mukola.contactapplication.model.peopleHelper.PeopleHelper;
import com.example.mukola.contactapplication.view.acitivities.archiveActivity.ArchiveActivity;
import com.example.mukola.contactapplication.view.acitivities.contact.ContactActivity;
import com.example.mukola.contactapplication.view.fragments.allContacts.AllContactsFragment;
import com.example.mukola.contactapplication.view.fragments.tinder.TinderFragment;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.api.services.people.v1.PeopleScopes;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CleanUpActivity extends AppCompatActivity implements CleanUpContract.ICleanUpView,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks,TinderFragment.OnTinderFragmentInteractionListener,
        AllContactsFragment.OnAllContactsFragmentInteractionListener{

    @OnClick(R.id.imageview_open_archive_tinder)
    void onArchiveClick(View view) {
        presenter.openArchive();
    }

    @BindView(R.id.progressBar_clean)
    ProgressBar progressBar;

    @NonNull
    private final int RC_INTENT = 200;

    @NonNull
    private final int RC_API_CHECK = 100;

    @NonNull
    private List<Contact> contacts;

    @NonNull
    private User user;

    @NonNull
    private TabLayout tabLayout;

    @NonNull
    private ViewPager viewPager;

    @NonNull
    private ViewPagerAdapter adapter;

    @NonNull
    private CleanUpContract.ICleanUpPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clean_up);
        ButterKnife.bind(this);

        presenter = new CleanUpPresenter(this,this,this);

        getData();

        showDialog();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    public void initViewPager(){
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {

        adapter = new ViewPagerAdapter(getSupportFragmentManager());

        TinderFragment tf = TinderFragment.newInstance(user.getId());
        adapter.addFragment(tf , this.getString(R.string.tinder));

        AllContactsFragment acf = AllContactsFragment.newInstance(user);
        adapter.addFragment(acf , this.getString(R.string.list));

        viewPager.setAdapter(adapter);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case RC_INTENT:
                presenter.verification(data);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
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
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        presenter.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void getIdToken() {
        Log.d("GET ID","TOKEN");

        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(presenter.getmGoogleApiClient());
        startActivityForResult(signInIntent, RC_INTENT);

    }

    @NonNull
    public List<Contact> getContacts() {
        return contacts;
    }

    @Override
    public void setContactList(List<Contact> contacts) {
        this.contacts = contacts;
        initViewPager();
    }

    @Override
    public void openArchive() {
        Intent intent = new Intent(this, ArchiveActivity.class);
        intent.putExtra("user",user);
        startActivity(intent);
    }

    @Override
    public void openContact(@NonNull Contact contact) {
        Intent intent = new Intent(this, ContactActivity.class);
        intent.putExtra("user",user);
        intent.putExtra("contact",contact);
        startActivity(intent);
    }

    @Override
    public void importPhone() {
        if (presenter.checkAndRequestPermissions(CleanUpPresenter.REQUEST_CONTACT_PERMISSIONS)) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    presenter.readContacts();
                }
            }).start();
        }
    }

    @Override
    public void setProgressBarVisible() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void setProgressBarGone() {
        progressBar.setVisibility(View.GONE);
    }

    private void getData() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            user = (User) extras.getSerializable("user");
        }
    }

    @Override
    public void showToast(@NonNull String message) {

    }

    @Override
    public void openTinder() {
        TinderFragment sf = TinderFragment.newInstance(user.getId());
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_content_c, sf)
                .addToBackStack("TinderFragment")
                .commit();
    }

    @Override
    public void openList() {
        AllContactsFragment af = AllContactsFragment.newInstance(user);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_content_c, af)
                .addToBackStack("ListFragment")
                .commit();
    }

    @Override
    public void onAllContactsFragmentOpenContract(@NonNull Contact contact) {
        presenter.openContact(contact);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    public void showDialog(){
        LayoutInflater factory = LayoutInflater.from(this);
        final View deleteDialogView = factory.inflate(R.layout.import_dialog, null);
        final AlertDialog deleteDialog = new AlertDialog.Builder(this).create();
        deleteDialog.setView(deleteDialogView);
        deleteDialogView.findViewById(R.id.button_alert_phone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                importPhone();
                deleteDialog.dismiss();
            }
        });
        deleteDialogView.findViewById(R.id.button_alert_google).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.InitGoogleSignIn();
                presenter.getIdToken();
                deleteDialog.dismiss();
            }
        });

        deleteDialogView.findViewById(R.id.button_alert_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("cancel");
                finish();
                deleteDialog.dismiss();
            }
        });

        deleteDialog.show();
    }




}
