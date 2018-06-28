package com.example.mukola.contactapplication.view.fragments.login;

import android.support.annotation.NonNull;

import com.example.mukola.contactapplication.model.models.User;

public interface LoginContract {
    public interface IRegisterView{
        void showToast(@NonNull String message);
        void signUpTvPressed();
        void signInButtonPressed(@NonNull String email,@NonNull String password);
        void signInGooglePressed();
    }

    public interface IRegisterPresenter{
        void signUpTvPressed();
        void signInButtonPressed(@NonNull String email,@NonNull String password);
        void signInGooglePressed();
        void detachView();
    }
}
