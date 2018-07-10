package com.example.mukola.contactapplication.view.acitivities.archiveActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mukola.contactapplication.R;
import com.example.mukola.contactapplication.model.models.Contact;
import com.example.mukola.contactapplication.model.models.User;
import com.example.mukola.contactapplication.view.acitivities.adapter.ArchiveListAdapter;
import com.example.mukola.contactapplication.view.acitivities.contact.ContactActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ArchiveActivity extends AppCompatActivity implements ArchiveContract.IImportView,ArchiveListAdapter.OnItemClicked



{

    @BindView(R.id.rv_contacts_is)
    RecyclerView list;

    @BindView(R.id.tv_no_contact_is)
    TextView tv;

    @BindView(R.id.swipeRefreshLayoutA)
    SwipeRefreshLayout swipeRefreshLayout;


    @OnClick(R.id.import_back)
    void onBackClick(View view) {
        onBackPressed();
    }

    @NonNull
    private ArchiveContract.IImportPresenter presenter;

    @NonNull
    private User user;

    private ArrayList<Contact> mSectionList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_is);
        setSupportActionBar(toolbar);
        toolbar.setSubtitleTextColor(Color.WHITE);

        setTitle(getString(R.string.archive));

        ButterKnife.bind(this);

        presenter = new ArchivePresenter(this,this,this);

        mSectionList = new ArrayList<>();

        getData();
        initRefresh();

        presenter.getArchive(user.getId());



    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    @Override
    public void setContactList(ArrayList<Contact> contacts) {

        if (contacts.size() == 0) {

           list.setVisibility(View.GONE);
           tv.setVisibility(View.VISIBLE);

        } else {
            mSectionList.clear();

            presenter.getHeaderListLatter(contacts,mSectionList);

            list.setVisibility(View.VISIBLE);
            list.setLayoutManager(new LinearLayoutManager(this));

            LinearLayoutManager lm = new LinearLayoutManager(this);
            lm.setOrientation(LinearLayoutManager.VERTICAL);
            list.setLayoutManager(lm);


            ArchiveListAdapter mAdapter = new ArchiveListAdapter(mSectionList, this);
            // set adapter
            mAdapter.setOnClick(this);
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
    public void setTvVisible() {
        list.setVisibility(View.GONE);
        tv.setVisibility(View.VISIBLE);
        tv.setText(getString(R.string.no_arc_contact));
    }


    @Override
    public void showToast(@NonNull String message) {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFavClick(@NonNull Contact contact) {
        contact.setFavorite(true);
        presenter.addToContact(user.getId(),contact);
        presenter.deleteFromArchive(user.getId(),contact.getId());
    }

    @Override
    public void onKeepClick(@NonNull Contact contact) {
        contact.setFavorite(false);
        presenter.addToContact(user.getId(),contact);
        presenter.deleteFromArchive(user.getId(),contact.getId());
        presenter.getArchive(user.getId());
    }

    @Override
    public void onUserClick(@NonNull Contact contact) {
        presenter.openContact(contact);
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
        presenter.getArchive(user.getId());
        onItemsLoadComplete();
    }

    void onItemsLoadComplete() {

        swipeRefreshLayout.setRefreshing(false);
    }
}
