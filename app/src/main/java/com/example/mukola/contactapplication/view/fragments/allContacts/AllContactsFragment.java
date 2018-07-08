package com.example.mukola.contactapplication.view.fragments.allContacts;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mukola.contactapplication.R;
import com.example.mukola.contactapplication.model.models.Contact;
import com.example.mukola.contactapplication.model.models.User;
import com.example.mukola.contactapplication.view.acitivities.adapter.ImportContactsListAdapter;
import com.example.mukola.contactapplication.view.acitivities.cleanUp.CleanUpActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class AllContactsFragment extends Fragment implements  AllContactsContract.IAllContactsView,
        ImportContactsListAdapter.OnItemClicked

{

    @BindView(R.id.rv_contacts_af)
    RecyclerView list;

    @BindView(R.id.tv_no_contact_af)
    TextView tv;

    @BindView(R.id.swipeRefreshLayoutAll)
    SwipeRefreshLayout swipeRefreshLayout;

    @NonNull
    private User user;

    private OnAllContactsFragmentInteractionListener mListener;

    @NonNull
    private Unbinder unbinder;

    @NonNull
    private AllContactsContract.IAllContactsPresenter presenter;

    private ArrayList<String> blacklist;

    public AllContactsFragment() {
        // Required empty public constructor
    }

    public static AllContactsFragment newInstance(@NonNull User user) {
        AllContactsFragment fragment = new AllContactsFragment();
        fragment.setUser(user);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_all_contacts, container, false);


        unbinder = ButterKnife.bind(this,view);

        presenter = new AllContactsPresenter(this,getActivity(),getContext());

        initRefresh();
        initList();

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnAllContactsFragmentInteractionListener) {
            mListener = (OnAllContactsFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnAllContactsFragmentInteractionListener");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(((CleanUpActivity) getActivity()).getContacts()==null){
            setImportButtonVisible();
        }else {
            setContactList(((CleanUpActivity) getActivity()).getContacts());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        unbinder.unbind();
        presenter.detachView();
        getActivity().finish();
    }

    private void initList(){
        presenter.getBlacklist(user.getId());
        if(((CleanUpActivity) getActivity()).getContacts()==null){
            setImportButtonVisible();
        }else {
            setContactList(((CleanUpActivity) getActivity()).getContacts());
        }
    }


    @Override
    public void setContactList(List<Contact> contacts) {

        ArrayList<Contact> l = new ArrayList<>();

        if (blacklist!=null) {
            for (Contact c : contacts) {
                boolean b = false;
                for (String s : blacklist) {
                    if (c.getBlacklistId().equals(s)) {
                        b = true;
                    }
                }

                if (!b) {
                    l.add(c);
                }
            }
        }

            list.setVisibility(View.VISIBLE);
            tv.setVisibility(View.GONE);
            list.setLayoutManager(new LinearLayoutManager(getActivity()));

            LinearLayoutManager lm = new LinearLayoutManager(getActivity());
            lm.setOrientation(LinearLayoutManager.VERTICAL);
            list.setLayoutManager(lm);



            ImportContactsListAdapter mAdapter = new ImportContactsListAdapter(l, getActivity(),getContext());
            // set adapter
            mAdapter.setOnClick(this);

            list.setAdapter(mAdapter);

            // set item animator to DefaultAnimator
            list.setItemAnimator(new DefaultItemAnimator());
    }




    @Override
    public void openContact(@NonNull Contact contact) {
        if (mListener != null) {
            mListener.onAllContactsFragmentOpenContract(contact);
        }
    }

    @Override
    public void setImportButtonVisible() {
        list.setVisibility(View.GONE);
        tv.setVisibility(View.VISIBLE);
    }

    @Override
    public void setBlacklist(ArrayList<String> list) {
        blacklist = list;
    }


    public void setUser(@NonNull User user){
        this.user = user;
    }


    @Override
    public void showToast(String message){
        Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAddClick(@NonNull Contact contact) {
        presenter.addToContact(user.getId(),contact);
        presenter.addToBlackList(user.getId(),contact.getBlacklistId());
        initList();
    }

    @Override
    public void onFavoriteClick(@NonNull Contact contact) {
        contact.setFavorite(true);
        presenter.addToContact(user.getId(),contact);
        presenter.addToBlackList(user.getId(),contact.getBlacklistId());
        initList();
    }

    @Override
    public void onArchiveClick(@NonNull Contact contact) {
        presenter.addToArchive(user.getId(),contact);
        presenter.addToBlackList(user.getId(),contact.getBlacklistId());
        initList();
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
        initList();
        onItemsLoadComplete();
    }

    void onItemsLoadComplete() {

        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onUserClick(@NonNull Contact contact) {
       presenter.openContact(contact);
    }

    public interface OnAllContactsFragmentInteractionListener {

        void onAllContactsFragmentOpenContract(@NonNull Contact contact);

    }

}
