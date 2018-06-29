package com.example.mukola.contactapplication.view.acitivities.mainScreen;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mukola.contactapplication.R;
import com.example.mukola.contactapplication.model.models.User;
import com.example.mukola.contactapplication.model.peopleHelper.PeopleHelper;
import com.example.mukola.contactapplication.view.acitivities.contact.ContactActivity;
import com.example.mukola.contactapplication.view.fragments.allContacts.AllContactsFragment;
import com.example.mukola.contactapplication.view.fragments.favoriteContacts.FavoriteContactsFragment;
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

import butterknife.ButterKnife;


public class MainScreenActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, MSContract.IMainScreenView,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks,AllContactsFragment.OnAllContactsFragmentInteractionListener,
        FavoriteContactsFragment.OnFavoriteContactsFragmentInteractionListener
{


    @NonNull
    private final int RC_INTENT = 200;

    @NonNull
    private final int RC_API_CHECK = 100;

    @Nullable
    private MSContract.IMainScreenPresenter presenter;

    @NonNull
    private User user;

    @Nullable
    private ArrayList<Person> contacts;

    private TabLayout tabLayout;

    private ViewPager viewPager;

    ViewPagerAdapter adapter;

    @NonNull
    private String authCode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ButterKnife.bind(this);

        getSupportActionBar().hide();

        presenter = new MSPresenter(this,this);

        user = getData();

        presenter.InitGoogleSignIn();

        presenter.getIdToken();


        Log.d("Logined users email - ", user.getEmail());
    }

    @Override
    public void initViewPager(ArrayList<Person> list){

        contacts = list;
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

    }

    private void setupViewPager(ViewPager viewPager) {

        adapter = new ViewPagerAdapter(getSupportFragmentManager());

        presenter.openAllContacts();

        presenter.openFavorite(user.getId());

        viewPager.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

//    @Override
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

    private User getData() {
        Bundle extras = getIntent().getExtras();
        User user = null;

        if (extras != null) {
            user = (User) extras.getSerializable("user");
        }
        return user;
    }


    @Override
    public void showToast(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void openAllContacts() {
        AllContactsFragment fr = AllContactsFragment.newInstance();
        adapter.addFragment(fr,this.getString(R.string.all_contacts) );
    }

    @Override
    public void openContact(Bundle person,int userId) {
        Intent intent = new Intent(this, ContactActivity.class);
        intent.putExtra("user",user);
        intent.putExtra("person",person);
        startActivity(intent);
    }

    @Override
    public void openFavorite(int userId) {
        FavoriteContactsFragment afr = FavoriteContactsFragment.newInstance(userId);
        adapter.addFragment(afr , this.getString(R.string.favorite_contacts));
    }

    @Nullable
    public ArrayList<Person> getContacts() {
        return contacts;
    }

    @Override
    public void onAllContactsFragmentInteraction(Person person) {
        presenter.openContact(createPersonBundle(person),user.getId());
    }

    @Override
    public void onFavoriteContactsFragmentInteraction(Person person) {
        presenter.openContact(createPersonBundle(person),user.getId());
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

    private Bundle createPersonBundle(Person p) {
        Bundle person = new Bundle();

        person.putString("resourceName",p.getResourceName());

        if (p.getNames() != null) {
            String nm = p.getNames().get(0).getDisplayName();
            if (nm != null) {
                person.putString("name", nm);
            } else {
                person.putString("name", getString(R.string.no_name));
            }
        }
        if (p.getPhoneNumbers() != null) {
            String ph = p.getPhoneNumbers().get(0).getCanonicalForm();
            if (ph != null) {
                person.putString("phone", ph);
            } else {
                person.putString("phone", getString(R.string.no_phone));
            }
        }
        if (p.getEmailAddresses() != null) {
            String em = p.getEmailAddresses().get(0).getValue();
            if (em != null) {
                person.putString("email", em);
            } else {
                person.putString("email", getString(R.string.no_email));
            }
        }
        if (p.getAddresses() != null) {
            String em = p.getAddresses().get(0).getCity();
            if (em != null) {
                person.putString("address", em);
            } else {
                person.putString("address", getString(R.string.no_address));
            }
        }
        if (p.getOrganizations() != null) {
            String em = p.getOrganizations().get(0).getName();
            if (em != null) {
                person.putString("company", em);
            } else {
                person.putString("company", getString(R.string.no_company));
            }
        }
        if (p.getPhotos() != null) {
            person.putString("url",p.getPhotos().get(0).getUrl());
        }

        return person;
    }

}



