package com.example.mukola.contactapplication.model.repositories;

import android.support.annotation.NonNull;

import com.example.mukola.contactapplication.model.models.User;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

public interface GoogleSignInRepository {
    public void signIn(@NonNull GoogleSignInAccount acct, @NonNull SignInCallback Callback);

    void logOut();
    public interface SignInCallback {
        void signIn(@NonNull User user);
    }
}
