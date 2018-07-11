package com.example.mukola.contactapplication.view.fragments.register;

import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

import java.util.List;

import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.app.Activity.RESULT_OK;


public class RegisterFragment extends Fragment implements RegisterContract.IRegisterView{

    private static final int RC_SIGN_UP = 2 ;

    private OnRegisterFragmentInteractionListener mListener;

    private Unbinder unbinder;

    private RegisterContract.IRegisterPresenter presenter;


    @BindViews({R.id.et_full_name_register,R.id.et_contact_number_register,
    R.id.et_adress_register,R.id.et_email_register,R.id.et_password_register,
    R.id.et_confirm_password_register})
    List<EditText> etList;

    @OnClick(R.id.tv_signin_register)
    void onSignInTvClick(View view) {
        presenter.openSignIn();
    }

    @OnClick(R.id.btn_signup_register)
    void onSignUpBtnClick(View view) {
        presenter.createUser(etList);
    }

    @OnClick(R.id.sign_in_button_register)
    void onSignUpGoogleClick(View view) {
        signUp();
    }

    public RegisterFragment() {
        // Required empty public constructor
    }


    public static RegisterFragment newInstance() {
        RegisterFragment fragment = new RegisterFragment();

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        unbinder = ButterKnife.bind(this,view);

        presenter = new RegisterPresenter(this,getContext());

        initGSO();
        return view;
    }

    private void initGSO(){
        if (!presenter.isOnline()) {
            showToast("Bad internet connection!");
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnRegisterFragmentInteractionListener) {
            mListener = (OnRegisterFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnRegisterFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        unbinder.unbind();
        presenter.detachView();
    }

    private void signUp() {
        Intent googlePicker = AccountPicker.newChooseAccountIntent(null, null, new String[]{GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE}, true, null, null, null, null);
        startActivityForResult(googlePicker, RC_SIGN_UP);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_UP && resultCode == RESULT_OK) {
            String accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
            presenter.createGoogleUser(accountName);
        }
    }

    @Override
    public void signInTvPressed(){
        if (mListener != null) {
            mListener.onSignInTvPressed();
        }
    }

    @Override
    public void openMainScreen(@NonNull User user) {
        mListener.openMainScreenFromRegisterFragment(user);
    }



    @Override
    public void showToast(@NonNull String message) {
        Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
    }


    public interface OnRegisterFragmentInteractionListener {
        // TODO: Update argument type and name
        void onSignInTvPressed();
        void openMainScreenFromRegisterFragment(@NonNull User user);
    }
}
