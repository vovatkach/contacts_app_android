package com.example.mukola.contactapplication.view.acitivities.main;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.ContentFrameLayout;
import android.util.Log;

import com.example.mukola.contactapplication.R;
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
import com.google.android.gms.tasks.Task;

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
    public void register(@NonNull final User user) {
        getUserRepository.getUser(user.getName(), new GetUserRepository.GetUserCallback() {
            @Override
            public void foundUser(@NonNull User user) {
                if (view!=null) {
                    view.showToast("Current user is already exists!");
                }
            }

            @Override
            public void notFound() {
                registerUser(user);
            }
        });
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
                    view.showToast("User not found!");
                }
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
//            Log.d("SERVER AAAAAA",account.getServerAuthCode());
            user.setAuthCode(account.getServerAuthCode());
            view.openMainScreen(user);
            Log.d("Google email",account.getEmail());
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("TAG", "signInResult:failed code=" + e.getStatusCode());
            view.showToast("Please, try again!");
            //updateUI(null);
        }
    }

    @Override
    public void signIn() {
        view.signIn();
    }

    @Override
    public GoogleSignInClient getGoogleSignInClient() {
        return mGoogleSignInClient;
    }

    @Override
    public void openMainScreen(@NonNull User user) {
        view.openMainScreen(user);
    }

    @Override
    public void detachView() {
        view = null;
    }


}
