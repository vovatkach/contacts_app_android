package com.example.mukola.contactapplication.view.fragments.allContacts;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mukola.contactapplication.R;
import com.example.mukola.contactapplication.model.models.Contact;
import com.example.mukola.contactapplication.model.models.User;
import com.example.mukola.contactapplication.view.acitivities.adapter.ContactListAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class AllContactsFragment extends Fragment implements  AllContactsContract.IAllContactsView,
        ContactListAdapter.OnItemClicked

{

    @BindView(R.id.rv_contacts_af)
    RecyclerView list;


    @BindView(R.id.tv_no_contact_af)
    TextView tv;

    @BindView(R.id.btn_import_af)
    Button btn;

    @OnClick(R.id.btn_import_af)
    void onImportClick(View view) {
        mListener.onImportClick();
    }

    @NonNull
    private User user;


    @NonNull
    private String pNumber;


    private OnAllContactsFragmentInteractionListener mListener;

    @NonNull
    private Unbinder unbinder;

    @NonNull
    private AllContactsContract.IAllContactsPresenter presenter;

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

        presenter = new AllContactsPresenter(this,getActivity(),this,getContext());

        presenter.getContacts(user.getId());

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
        new Thread(new Runnable() {
            @Override
            public void run() {
                presenter.getContacts(user.getId());
            }
        }).start();


    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        unbinder.unbind();
        presenter.detachView();
        getActivity().finish();
    }


    @Override
    public void setContactList(List<Contact> contacts) {

            list.setVisibility(View.VISIBLE);
            btn.setVisibility(View.GONE);
            tv.setVisibility(View.GONE);
            list.setLayoutManager(new LinearLayoutManager(getActivity()));

            LinearLayoutManager lm = new LinearLayoutManager(getActivity());
            lm.setOrientation(LinearLayoutManager.VERTICAL);
            list.setLayoutManager(lm);

            ArrayList<Contact> l = new ArrayList<>(contacts);

            ContactListAdapter mAdapter = new ContactListAdapter(l, getActivity());
            // set adapter
            mAdapter.setOnClick(this);

            list.setAdapter(mAdapter);

            // set item animator to DefaultAnimator
            list.setItemAnimator(new DefaultItemAnimator());
    }


    @Override
    public void makeCall(final String number) {
        pNumber = number;

        if (presenter.checkAndRequestPermissions(AllContactsPresenter.REQUEST_ID_CALL_PERMISSIONS)) {
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:" + number));
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            startActivity(intent);
        }
    }

    @Override
    public void onContactClicked(@NonNull Contact contact) {
        if (mListener != null) {
            mListener.onAllContactsFragmentInteraction(contact);
        }
    }

    @Override
    public void setImportButtonVisible() {
        list.setVisibility(View.GONE);
        tv.setVisibility(View.VISIBLE);
        btn.setVisibility(View.VISIBLE);
    }

    @Override
    public void sendMessage(final String number){
        pNumber = number;
        if(presenter.checkAndRequestPermissions(AllContactsPresenter.REQUEST_ID_SMS_PERMISSIONS)) {

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("sms:" + number));
            startActivity(intent);
        }
    }

    public void setUser(@NonNull User user){
        this.user = user;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        presenter.onRequestPermissionsResult(requestCode, permissions, grantResults,pNumber);
    }

    @Override
    public void onCallClick(@NonNull String number) {
        presenter.makeCall(number);
    }

    @Override
    public void onMessageClick(@NonNull String number) {
        presenter.sendMessage(number);
    }

    @Override
    public void onUserClick(@NonNull Contact contact) {
        presenter.onContactClicked(contact);
    }

    @Override
    public void showToast(String message){
        Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
    }

    public interface OnAllContactsFragmentInteractionListener {

        void onAllContactsFragmentInteraction(@NonNull Contact contact);
        void onImportClick();

        }

}
