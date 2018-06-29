package com.example.mukola.contactapplication.view.fragments.favoriteContacts;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.mukola.contactapplication.R;
import com.example.mukola.contactapplication.model.peopleHelper.PeopleHelper;
import com.example.mukola.contactapplication.view.acitivities.adapter.ContactListAdapter;
import com.example.mukola.contactapplication.view.acitivities.mainScreen.MainScreenActivity;
import com.example.mukola.contactapplication.view.fragments.allContacts.AllContactsFragment;
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
import butterknife.Unbinder;

public class FavoriteContactsFragment extends Fragment implements FavoriteContactsContract.IFavoriteContactsView,
        ContactListAdapter.OnItemClicked
{


    @BindView(R.id.rv_contacts_fc)
    RecyclerView list;

    @BindView(R.id.progressBar2)
    ProgressBar progressBar;


    @NonNull
    private String pNumber;

    private OnFavoriteContactsFragmentInteractionListener mListener;

    @NonNull
    private Unbinder unbinder;

    @NonNull
    private FavoriteContactsContract.IFavoriteContactsPresenter presenter;

    @NonNull
    private int userId;


    public FavoriteContactsFragment() {
        // Required empty public constructor
    }



    public static FavoriteContactsFragment newInstance(int userId) {
        FavoriteContactsFragment fragment = new FavoriteContactsFragment();
        fragment.setUserId(userId);
      //  fragment.setAuthCode(authCode);
        return fragment;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favorite_contacts, container, false);

        unbinder = ButterKnife.bind(this,view);

        presenter = new FavoriteContactsPresenter(this,getActivity(),this,getContext());

        presenter.getFavorites(userId);

        presenter.checkFavorites( ((MainScreenActivity) getActivity()).getContacts() );

        return view;


    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.getFavorites(userId);

        presenter.checkFavorites( ((MainScreenActivity) getActivity()).getContacts() );
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFavoriteContactsFragmentInteractionListener) {
            mListener = (OnFavoriteContactsFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnLoginFragmentInteractionListener");
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        unbinder.unbind();
        presenter.detachView();
    }

    @Override
    public void setContactList(List<Person> contacts) {

        list.setLayoutManager(new LinearLayoutManager(getActivity()));

        LinearLayoutManager lm = new LinearLayoutManager(getActivity());
        lm.setOrientation(LinearLayoutManager.VERTICAL);
        list.setLayoutManager(lm);

        ArrayList<Person> l = new ArrayList<>(contacts);


        ContactListAdapter mAdapter = new ContactListAdapter(l, getActivity());
        // set adapter
        mAdapter.setOnClick(this);

        progressBar.setVisibility(View.GONE);

        if (mAdapter.getItemCount() == 0) {
            showToast(getString(R.string.no_contact));
        } else {
            list.setAdapter(mAdapter);
        }

        // set item animator to DefaultAnimator
        list.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public void makeCall(final String number) {
        pNumber = number;

        if (presenter.checkAndRequestPermissions(FavoriteContactsPresenter.REQUEST_ID_CALL_PERMISSIONS)) {
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:" + number));
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            startActivity(intent);

        }
    }

    @Override
    public void sendMessage(final String number){
        pNumber = number;
        if(presenter.checkAndRequestPermissions(FavoriteContactsPresenter.REQUEST_ID_SMS_PERMISSIONS)) {

            // This method will be executed once the timer is over
            // Start your app main activity
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("sms:" + number));
            startActivity(intent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        Log.d("PERMIS","FRAGMENT");
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        presenter.onRequestPermissionsResult(requestCode, permissions, grantResults,pNumber);
    }

    @Override
    public void onCallClick(String number) {
        presenter.makeCall(number);
    }

    @Override
    public void onMessageClick(String number) {
        presenter.sendMessage(number);
    }

    @Override
    public void onUserClick(int position) {
        presenter.onContactClicked(((MainScreenActivity)getActivity()).getContacts().get(position));
    }

    @Override
    public void onContactClicked(Person person) {
        if (mListener != null) {
            mListener.onFavoriteContactsFragmentInteraction(person);
        }
    }

    @Override
    public void showToast(String message){
        Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setProgressBarVisible() {
        progressBar.setVisibility(View.VISIBLE);
    }

    public void setUserId(@NonNull int userId) {
        this.userId = userId;
    }


    public interface OnFavoriteContactsFragmentInteractionListener {
        void onFavoriteContactsFragmentInteraction(Person person);
    }

}
