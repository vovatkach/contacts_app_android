package com.example.mukola.contactapplication.view.fragments.register;

import android.content.Context;
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

import java.util.List;

import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class RegisterFragment extends Fragment implements RegisterContract.IRegisterView{



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
        presenter.signInWithGoogle();
    }

    public RegisterFragment() {
        // Required empty public constructor
    }


    public static RegisterFragment newInstance() {
        RegisterFragment fragment = new RegisterFragment();

        return fragment;
    }

    @Override
    public void signInTvPressed(){
        if (mListener != null) {
            mListener.onSignInTvPressed();
        }
    }

    @Override
    public void signUpButtonPressed(User user) {
        if (mListener != null) {
            mListener.onSignUpBtnPressed(user);
        }
    }

    @Override
    public void signInGooglePressed() {
        if (mListener != null) {
            mListener.onGoogleSignUpPressed();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        unbinder = ButterKnife.bind(this,view);

        presenter = new RegisterPresenter(this);

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
    public void showToast(@NonNull String message) {
        Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
    }


    public interface OnRegisterFragmentInteractionListener {
        // TODO: Update argument type and name
        void onSignInTvPressed();
        void onSignUpBtnPressed(@NonNull User user);
        void onGoogleSignUpPressed();
    }
}
