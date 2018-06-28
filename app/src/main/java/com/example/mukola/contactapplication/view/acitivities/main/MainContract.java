package com.example.mukola.contactapplication.view.acitivities.main;

import android.support.annotation.NonNull;

import com.example.mukola.contactapplication.model.models.User;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.Task;

public interface MainContract {
    public interface IMainView{
        void openLoginFragment();
        void openRegisterFragment();
        void showToast(@NonNull String message);
        void signIn();
        void openMainScreen(@NonNull User user);

    }

    public interface IMainPresenter{
        void openLoginFragment();
        void openRegisterFragment();
        void register(@NonNull User user);
        void login(@NonNull String email,@NonNull String password);
        void handleSignInResult(Task<GoogleSignInAccount> completedTask);
        void signIn();
        GoogleSignInClient getGoogleSignInClient();
        void openMainScreen(@NonNull User user);
        void detachView();
    }
}
