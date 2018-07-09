package com.example.mukola.contactapplication.view.acitivities.mainScreen;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mukola.contactapplication.R;
import com.example.mukola.contactapplication.model.models.Contact;
import com.example.mukola.contactapplication.model.models.User;
import com.example.mukola.contactapplication.view.acitivities.adapter.ContactListAdapter;
import com.example.mukola.contactapplication.view.acitivities.addContact.AddContactActivity;
import com.example.mukola.contactapplication.view.acitivities.cityReminder.ReminderActivity;
import com.example.mukola.contactapplication.view.acitivities.cleanUp.CleanUpActivity;
import com.example.mukola.contactapplication.view.acitivities.contact.ContactActivity;
import com.example.mukola.contactapplication.view.acitivities.favorite.FavoriteActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MainScreenActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, MSContract.IMainScreenView,
         ContactListAdapter.OnItemClicked {

    @BindView(R.id.rv_contacts_ms)
    RecyclerView list;

    @BindView(R.id.tv_no_contact_ms)
    TextView tv;

    @BindView(R.id.btn_import_ms)
    Button btn;



    @BindView(R.id.swipeRefreshLayoutMain)
    SwipeRefreshLayout swipeRefreshLayout;

    @OnClick(R.id.btn_import_ms)
    void onImportClick(View view) {
        presenter.openCleanUp();
    }

    @OnClick(R.id.imageview_favorite)
    void onFavoriteClick(View view) {
        presenter.openFavorite();
    }

    @Nullable
    private MSContract.IMainScreenPresenter presenter;

    @NonNull
    private User user;

    @NonNull
    private FloatingActionButton fab;

    @NonNull
    private  TextView  tv_email_drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_m);
        setSupportActionBar(toolbar);
         fab = (FloatingActionButton) findViewById(R.id.fab);
         setOnFabListener();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ButterKnife.bind(this);

        presenter = new MSPresenter(this,this,this);

        user = getData();

        initView();
        initRefresh();

        presenter.getContacts(user.getId());
        Log.d("Logined users email - ", user.getEmail());
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.getContacts(user.getId());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    private void setOnFabListener(){
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.openCreateContact();
            }
        });
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

        if (id == R.id.nav_import) {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

            drawer.closeDrawer(GravityCompat.START);
        } else if (id == R.id.nav_reminder) {
            presenter.openCityReminder();
        } else if (id == R.id.nav_clean) {
            presenter.openCleanUp();

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
    public void openContact(@NonNull Contact contact, int userId) {
        Intent intent = new Intent(this, ContactActivity.class);
        intent.putExtra("user",user);
        intent.putExtra("contact",contact);
        startActivity(intent);
    }


    @Override
    public void openCleanUp() {
        Intent intent = new Intent(this, CleanUpActivity.class);
        intent.putExtra("user",user);
        startActivity(intent);
    }

    @Override
    public void openCreateContact() {
        Intent intent = new Intent(this, AddContactActivity.class);
        intent.putExtra("user",user);
        startActivity(intent);
    }

    @Override
    public void openCityReminder() {
        Intent intent = new Intent(this, ReminderActivity.class);
        intent.putExtra("user",user);
        startActivity(intent);
    }


    @Override
    public void setImportButtonVisible() {
        list.setVisibility(View.GONE);
        tv.setVisibility(View.VISIBLE);
        btn.setVisibility(View.VISIBLE);
    }

    @Override
    public void setContactList(List<Contact> contacts) {

        onItemsLoadComplete();
        list.setVisibility(View.VISIBLE);
        btn.setVisibility(View.GONE);
        tv.setVisibility(View.GONE);
        list.setLayoutManager(new LinearLayoutManager(this));

        LinearLayoutManager lm = new LinearLayoutManager(this);
        lm.setOrientation(LinearLayoutManager.VERTICAL);
        list.setLayoutManager(lm);

        ArrayList<Contact> l = new ArrayList<>(contacts);

        ContactListAdapter mAdapter = new ContactListAdapter(l, this);
        // set adapter
        mAdapter.setOnClick(this);

        list.setAdapter(mAdapter);

        // set item animator to DefaultAnimator
        list.setItemAnimator(new DefaultItemAnimator());
    }

    public void initView(){
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);

        tv_email_drawer = headerView.findViewById(R.id.textView_user_email_draw);

        tv_email_drawer.setText(user.getEmail());
    }

    @Override
    public void openFavorite() {
        Intent intent = new Intent(this, FavoriteActivity.class);
        intent.putExtra("user",user);
        startActivity(intent);
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
        presenter.openContact(contact,user.getId());
    }


    private void initRefresh(){
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                refreshItems();
            }
        });


    }
    void refreshItems() {
        presenter.getContacts(user.getId());
        onItemsLoadComplete();
    }

    void onItemsLoadComplete() {

        swipeRefreshLayout.setRefreshing(false);
    }
}



