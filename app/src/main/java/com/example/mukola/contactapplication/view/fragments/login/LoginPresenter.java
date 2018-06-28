package com.example.mukola.contactapplication.view.fragments.login;

import android.support.annotation.NonNull;

import com.example.mukola.contactapplication.model.models.User;

public class LoginPresenter implements LoginContract.IRegisterPresenter {

    @NonNull
    private LoginContract.IRegisterView view;

    public LoginPresenter (LoginContract.IRegisterView view){
        this.view = view;
    }

    @Override
    public void detachView(){
        view = null;
    }

    @Override
    public void signUpTvPressed() {
        view.signUpTvPressed();
    }

    @Override
    public void signInButtonPressed(@NonNull String email,@NonNull String password) {
        if (isEmpty(email, password)==false) {
            view.signInButtonPressed(email, password);
        }
    }

    @Override
    public void signInGooglePressed() {
        view.signInGooglePressed();
    }

    private boolean isEmpty(String email,String password){
        if (email.isEmpty()){
            view.showToast("Please enter your email!");
            return true;
        }else if (password.isEmpty()){
            view.showToast("Please enter your password!");
            return true;
        } else {
            return false;
        }
    }



}
