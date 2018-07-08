package com.example.mukola.contactapplication.view.acitivities.main;

import android.content.Context;
import android.support.annotation.NonNull;

import com.example.mukola.contactapplication.model.models.User;
import com.example.mukola.contactapplication.model.repositories.GetUserRepository;
import com.example.mukola.contactapplication.model.repositories.GetUserRepositoryImpl;
import com.example.mukola.contactapplication.model.repositories.RegisterRepository;
import com.example.mukola.contactapplication.model.repositories.RegisterRepositoryImpl;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

public class MainPresenter implements MainContract.IMainPresenter {

    @NonNull
    private Context context;

    @NonNull
    private RegisterRepository registerRepository;

    @NonNull
    private GetUserRepository getUserRepository;

    @NonNull
    private GoogleSignInClient mGoogleSignInClient;


    @NonNull
    private MainContract.IMainView view;

    public MainPresenter(@NonNull Context context,@NonNull MainContract.IMainView view){
        this.context = context;
        this.view = view;
        registerRepository = new RegisterRepositoryImpl(context);
        getUserRepository = new GetUserRepositoryImpl(context);
        initGoogleSign();
    }

    private void initGoogleSign(){
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(context, gso);
    }

    private void checkIsSigned(){
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(context);
        //updateUI(account);
    }

    @Override
    public void openLoginFragment() {
        view.openLoginFragment();
    }

    @Override
    public void openRegisterFragment() {
        view.openRegisterFragment();
    }

    @Override
    public void openMainScreen(@NonNull User user) {
        view.openMainScreen(user);
    }

    @Override
    public void detachView() {
        view = null;
    }

    @Override
    public void openStartFragment() {
        view.openStartFragment();
    }


}
