package com.example.mukola.contactapplication.view.fragments.start;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mukola.contactapplication.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class StartFragment extends Fragment {

    @NonNull
    private Unbinder unbinder;

    private OnStartFragmentInteractionListener mListener;

    @OnClick(R.id.btn_login)
    void onSignInClick(View view) {
        if (mListener != null) {
            mListener.onLoginClick();
        }    }

    @OnClick(R.id.btn_register)
    void onSignUpClick(View view) {
        if (mListener != null) {
            mListener.onRegisterClick();
        }
    }


    public StartFragment() {
        // Required empty public constructor
    }

    public static StartFragment newInstance(String param1, String param2) {
        StartFragment fragment = new StartFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_start, container, false);

        unbinder = ButterKnife.bind(this,view);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnStartFragmentInteractionListener) {
            mListener = (OnStartFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnStartFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        unbinder.unbind();
        getActivity().finish();
    }


    public interface OnStartFragmentInteractionListener {
        void onLoginClick();
        void onRegisterClick();
    }
}
