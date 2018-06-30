package com.example.mukola.contactapplication.view.fragments.register;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mukola.contactapplication.R;
import com.example.mukola.contactapplication.model.models.User;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.Task;

import java.util.List;

import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class RegisterFragment extends Fragment implements RegisterContract.IRegisterView{

    private static final int RC_SIGN_IN = 1;

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
        presenter.signIn();
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

        return view;
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            presenter.handleSignInResult(task);
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
    public void signIn() {
        Intent signInIntent = presenter.getGoogleSignInClient().getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
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
