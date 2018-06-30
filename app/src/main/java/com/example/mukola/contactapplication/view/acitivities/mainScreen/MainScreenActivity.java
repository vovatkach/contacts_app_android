package com.example.mukola.contactapplication.view.acitivities.mainScreen;

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

import com.example.mukola.contactapplication.R;
import com.example.mukola.contactapplication.model.models.Contact;
import com.example.mukola.contactapplication.model.models.User;
import com.example.mukola.contactapplication.view.acitivities.contact.ContactActivity;
import com.example.mukola.contactapplication.view.acitivities.importActivity.ImportActivity;
import com.example.mukola.contactapplication.view.fragments.allContacts.AllContactsFragment;
import com.example.mukola.contactapplication.view.fragments.favoriteContacts.FavoriteContactsFragment;
import com.google.api.services.people.v1.model.Person;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;


public class MainScreenActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, MSContract.IMainScreenView,
        AllContactsFragment.OnAllContactsFragmentInteractionListener,
        FavoriteContactsFragment.OnFavoriteContactsFragmentInteractionListener
{




    @Nullable
    private MSContract.IMainScreenPresenter presenter;

    @NonNull
    private User user;

    private TabLayout tabLayout;

    private ViewPager viewPager;

    ViewPagerAdapter adapter;


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

        initViewPager();

        Log.d("Logined users email - ", user.getEmail());
    }

    public void initViewPager(){
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




    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            presenter.openImport();
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
        AllContactsFragment fr = AllContactsFragment.newInstance(user);
        adapter.addFragment(fr,this.getString(R.string.all_contacts) );
    }

    @Override
    public void openContact(@NonNull Contact contact, int userId) {
        Intent intent = new Intent(this, ContactActivity.class);
        intent.putExtra("user",user);
        intent.putExtra("contact",contact);
        startActivity(intent);
    }

    @Override
    public void openFavorite(int userId) {
        FavoriteContactsFragment afr = FavoriteContactsFragment.newInstance(userId);
        adapter.addFragment(afr , this.getString(R.string.favorite_contacts));
    }

    @Override
    public void openImport() {
        Intent intent = new Intent(this, ImportActivity.class);
        intent.putExtra("user",user);
        startActivity(intent);
    }

    @Override
    public void onAllContactsFragmentInteraction(@NonNull Contact contact) {
        presenter.openContact(contact,user.getId());
    }

    @Override
    public void onImportClick() {
        presenter.openImport();
    }

    @Override
    public void onFavoriteContactsFragmentInteraction(@NonNull Contact contact) {
        presenter.openContact(contact,user.getId());
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

}



