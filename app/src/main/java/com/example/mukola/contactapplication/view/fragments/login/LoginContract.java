package com.example.mukola.contactapplication.view.fragments.login;

import android.support.annotation.NonNull;

import com.example.mukola.contactapplication.model.models.User;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.Task;

public interface LoginContract {
    public interface IRegisterView{
        void showToast(@NonNull String message);
        void signUpTvPressed();
        void openMainScreen(@NonNull User user);

    }

    public interface IRegisterPresenter{
        void signUpTvPressed();
        void signInButtonPressed(@NonNull String email,@NonNull String password);
        void detachView();
        void login(@NonNull String email,@NonNull final String password);
        void firebaseAuthWithGoogleR(GoogleSignInAccount account);
        boolean isOnline();
    }
}
