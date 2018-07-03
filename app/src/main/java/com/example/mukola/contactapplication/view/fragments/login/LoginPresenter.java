package com.example.mukola.contactapplication.view.fragments.login;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.mukola.contactapplication.model.models.User;
import com.example.mukola.contactapplication.model.repositories.GetUserRepository;
import com.example.mukola.contactapplication.model.repositories.GetUserRepositoryImpl;
import com.example.mukola.contactapplication.model.repositories.RegisterRepository;
import com.example.mukola.contactapplication.model.repositories.RegisterRepositoryImpl;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;

public class LoginPresenter implements LoginContract.IRegisterPresenter {

    @NonNull
    private LoginContract.IRegisterView view;

    @NonNull
    private Context context;

    @NonNull
    private Activity activity;

    @NonNull
    private GetUserRepository getUserRepository;

    @NonNull
    private GoogleSignInClient mGoogleSignInClient;

    @NonNull
    private RegisterRepository registerRepository;


    public LoginPresenter (LoginContract.IRegisterView view,@NonNull Context context,@NonNull Activity activity){
        this.view = view;
        this.context = context;
        this.activity = activity;
        getUserRepository = new GetUserRepositoryImpl(context);
        registerRepository = new RegisterRepositoryImpl(context);
        initGoogleSign();
    }

    private void initGoogleSign(){
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        if(isOnline()) {
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();
            // Build a GoogleSignInClient with the options specified by gso.
            mGoogleSignInClient = GoogleSignIn.getClient(context, gso);
        }else {
            view.showToast("Bad internet connection.");
        }
    }

    private GoogleSignInAccount checkIsSigned(){
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(context);

        return account;
   }

    @Override
    public void login(@NonNull String email,@NonNull final String password) {
        getUserRepository.getUser(email, new GetUserRepository.GetUserCallback() {
            @Override
            public void foundUser(@NonNull User user) {
                if (view != null) {
                    if (user.getPassword().equals(password)) {
                        view.openMainScreen(user);
                    } else {
                        view.showToast("Incorrect password");
                        }
                    }
            }

            @Override
            public void notFound() {
                if (view != null) {
                    signOut();
                    view.showToast("User not found!");
                }
            }
        });
    }

    @Override
    public void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            User user = new User();
            user.setName(account.getDisplayName());
            user.setEmail(account.getEmail());
            user.setPassword(account.getId());
            user.setNumber(account.getFamilyName());
            user.setAddress(account.getGivenName());
            register(user);
            Log.d("Google email",account.getEmail());
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("TAG", "signInResult:failed code=" + e.getStatusCode());
            view.showToast("Please, try again!");
            //updateUI(null);
        }
    }

    private void handleSignIn(GoogleSignInAccount account) {
            User user = new User();
            user.setName(account.getDisplayName());
            user.setEmail(account.getEmail());
            user.setPassword(account.getId());
            user.setNumber(account.getFamilyName());
            user.setAddress(account.getGivenName());
            register(user);
            Log.d("Google email",account.getEmail());
    }

    private void register(@NonNull final User user) {
        getUserRepository.getUser(user.getEmail(), new GetUserRepository.GetUserCallback() {
            @Override
            public void foundUser(@NonNull User user) {

                if (view!=null) {
                    view.openMainScreen(user);
                }
            }

            @Override
            public void notFound() {
                registerUser(user);
            }
        });
    }



    private void registerUser(@NonNull User user){
        registerRepository.register(user, new RegisterRepository.RegisterCallback() {
            @Override
            public void register(@NonNull User user) {
                getUserRepository.getUser(user.getEmail(), new GetUserRepository.GetUserCallback() {
                    @Override
                    public void foundUser(@NonNull User user) {
                        if (view != null) {
                            view.openMainScreen(user);
                        }
                    }

                    @Override
                    public void notFound() {
                        if (view != null) {
                            view.showToast("Registration Failed");
                        }
                    }
                });
            }
        });
    }


    @Override
    public void signIn() {
        GoogleSignInAccount account = checkIsSigned();

        if (account!=null){
            handleSignIn(account);
        }else {
            view.signIn();
        }
    }

    @Override
    public GoogleSignInClient getGoogleSignInClient() {
        return mGoogleSignInClient;
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
            login(email,password);
        }
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

    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(activity, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...
                    }
                });
    }



    private boolean isOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        }
        catch (IOException e)          { e.printStackTrace(); }
        catch (InterruptedException e) { e.printStackTrace(); }

        return false;
    }



}
