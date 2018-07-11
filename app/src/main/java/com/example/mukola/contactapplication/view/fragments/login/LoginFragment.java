package com.example.mukola.contactapplication.view.fragments.login;

import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mukola.contactapplication.R;
import com.example.mukola.contactapplication.model.models.User;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.AccountPicker;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.app.Activity.RESULT_OK;


public class LoginFragment extends Fragment implements LoginContract.IRegisterView{

    private static final int RC_SIGN_IN = 1;

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
        signIn();
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

        presenter = new LoginPresenter(this,getContext());

        initGSO();

        return view;
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
    }

    private void initGSO(){
        if (!presenter.isOnline()) {
            showToast("Bad internet connection!");
        }
    }

    private void signIn() {
        Intent googlePicker = AccountPicker.newChooseAccountIntent(null, null, new String[]{GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE}, true, null, null, null, null);
        startActivityForResult(googlePicker, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN && resultCode == RESULT_OK) {
            String accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
            presenter.createGoogleUser(accountName);
        }
    }


    @Override
    public void showToast(@NonNull String message) {
        Toast.makeText(getActivity(),message,Toast.LENGTH_LONG).show();
    }

    @Override
    public void signUpTvPressed() {
        if (mListener != null) {
            mListener.onSignUpTVPressed();
        }
    }


    @Override
    public void openMainScreen(@NonNull User user) {
        if (mListener != null) {
            mListener.openMainScreenFromLoginFragment(user);
        }
    }


    public interface OnLoginFragmentInteractionListener {
        // TODO: Update argument type and name
        void onSignUpTVPressed();
        void openMainScreenFromLoginFragment(@NonNull User user);

    }
}
