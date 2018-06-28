package com.example.mukola.contactapplication.view.fragments.login;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mukola.contactapplication.R;
import com.example.mukola.contactapplication.model.models.User;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class LoginFragment extends Fragment implements LoginContract.IRegisterView{



    @Nullable
    private OnLoginFragmentInteractionListener mListener;

    @Nullable
    private Unbinder unbinder;

    @Nullable
    private LoginContract.IRegisterPresenter presenter;

    @BindView(R.id.et_email_login)
    EditText etEmail;

    @BindView(R.id.et_password_login)
    EditText etPassword;

    @OnClick(R.id.btn_signin_login)
    void onButtonLoginClick(View view) {
       presenter.signInButtonPressed(etEmail.getText().toString(),etPassword.getText().toString());
    }

    @OnClick(R.id.tv_signup_login)
    void onTVRegClick(View view) {
        presenter.signUpTvPressed();
    }

    @OnClick(R.id.sign_in_button_login)
    void onGoogleSignInClick(View view) {
        presenter.signInGooglePressed();
    }

    public LoginFragment() {
        // Required empty public constructor
    }

    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();

        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        unbinder = ButterKnife.bind(this,view);

        presenter = new LoginPresenter(this);

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed() {

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnLoginFragmentInteractionListener) {
            mListener = (OnLoginFragmentInteractionListener) context;
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
        getActivity().finish();
    }

    @Override
    public void showToast(@NonNull String message) {
        Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void signUpTvPressed() {
        if (mListener != null) {
            mListener.onSignUpTVPressed();
        }
    }

    @Override
    public void signInButtonPressed(@NonNull String email,@NonNull String password) {
        if (mListener != null) {
            mListener.onLoginBtnPressed(email,password);
        }
    }

    @Override
    public void signInGooglePressed() {
        if (mListener != null) {
            mListener.onGoogleSignInPressed();
        }
    }


    public interface OnLoginFragmentInteractionListener {
        // TODO: Update argument type and name
        void onSignUpTVPressed();
        void onLoginBtnPressed(@NonNull String email, @NonNull String password);
        void onGoogleSignInPressed();

    }
}
