package com.example.mukola.contactapplication.view.acitivities.favorite;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

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

public class FavoriteActivity extends AppCompatActivity implements FavoritContract.IFavoriteView, ContactListAdapter.OnItemClicked {

    @BindView(R.id.rv_contacts_af)
    RecyclerView list;

    @BindView(R.id.tv_no_contact_af)
    TextView tv;

    @BindView(R.id.swipeRefreshLayoutFav)
    SwipeRefreshLayout swipeRefreshLayout;

    @OnClick(R.id.favorite_back)
    void onFavoriteClick(View view) {
        onBackPressed();
    }

    @NonNull
    private User user;

    @NonNull
    private FavoritContract.IFavoritePresenter presenter;

    private ArrayList<Contact> mSectionList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        ButterKnife.bind(this);

        presenter = new FavoritePresenter(this,this);

        mSectionList = new ArrayList<>();

        getData();

        initRefresh();

        presenter.getFavorites(user.getId());

    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.getFavorites(user.getId());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    @Override
    public void setContactList(ArrayList<Contact> contacts) {

        mSectionList.clear();

        presenter.getHeaderListLatter(contacts,mSectionList);

        list.setVisibility(View.VISIBLE);
        tv.setVisibility(View.GONE);

        list.setLayoutManager(new LinearLayoutManager(this));

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

    private void getData() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            user = (User) extras.getSerializable("user");
        }
    }

    @Override
    public void onFavClick(@NonNull Contact contact) {
        if (contact.isFavorite()){
            presenter.deleteFRomFavorite(user.getId(),contact);
            refreshItems();
        }else {
            presenter.addToFavorite(user.getId(),contact);
        }
    }

    @Override
    public void onUserClick(@NonNull Contact contact) {
        presenter.onContactClicked(contact);
    }


    @Override
    public void onContactClicked(@NonNull Contact contact) {
        presenter.openContact(contact,user.getId());
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
    public void setvNoFavoriteVisible() {
        list.setVisibility(View.GONE);
        tv.setVisibility(View.VISIBLE);
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
        presenter.getFavorites(user.getId());
        onItemsLoadComplete();
    }

    void onItemsLoadComplete() {
        swipeRefreshLayout.setRefreshing(false);
    }
}
