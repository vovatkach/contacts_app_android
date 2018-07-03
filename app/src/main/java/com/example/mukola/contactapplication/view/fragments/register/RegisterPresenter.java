package com.example.mukola.contactapplication.view.fragments.register;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.EditText;

import com.example.mukola.contactapplication.model.models.User;
import com.example.mukola.contactapplication.model.repositories.GetUserRepository;
import com.example.mukola.contactapplication.model.repositories.GetUserRepositoryImpl;
import com.example.mukola.contactapplication.model.repositories.RegisterRepository;
import com.example.mukola.contactapplication.model.repositories.RegisterRepositoryImpl;
import com.example.mukola.contactapplication.view.acitivities.contact.ContactActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;

public class RegisterPresenter implements RegisterContract.IRegisterPresenter {

    @NonNull
    private RegisterContract.IRegisterView view;

    @NonNull
    private Context context;

    @NonNull
    private RegisterRepository registerRepository;

    @NonNull
    private GetUserRepository getUserRepository;

    @NonNull
    private GoogleSignInClient mGoogleSignInClient;

    @NonNull
    private Activity activity;



    public RegisterPresenter (@NonNull RegisterContract.IRegisterView view, @NonNull Context context,@NonNull Activity activity){
        this.view = view;
        this.context = context;
        this.activity = activity;
        registerRepository = new RegisterRepositoryImpl(context);
        getUserRepository = new GetUserRepositoryImpl(context);
        initGoogleSign();
        signOut();
    }

    private void initGoogleSign(){
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.

        if (isOnline()) {
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();
            // Build a GoogleSignInClient with the options specified by gso.
            mGoogleSignInClient = GoogleSignIn.getClient(context, gso);
        }else {
            view.showToast("Bad internet connection.");
        }
    }

    private void checkIsSigned(){
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(context);
        //updateUI(account);
    }

    @Override
    public void register(@NonNull final User user, final String type) {
        getUserRepository.getUser(user.getEmail(), new GetUserRepository.GetUserCallback() {
            @Override
            public void foundUser(@NonNull User user) {
                if (type.equals("google")){
                    if (view!=null) {
                        view.openMainScreen(user);
                    }
                }else {
                    if (view != null) {
                        view.showToast("Current user is already exists!");
                    }
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
            register(user,"google");
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
    public void createUser(@NonNull List<EditText> list){
        if (isEmpty(list)==false) {

            if (checkIsPasswordsMatch(list.get(4).getText().toString(),
                    list.get(5).getText().toString()) == true) {
                User user = new User();
                user.setName(list.get(0).getText().toString());
                user.setNumber(list.get(1).getText().toString());
                user.setAddress(list.get(2).getText().toString());
                user.setEmail(list.get(3).getText().toString());
                user.setPassword(list.get(4).getText().toString());
                register(user,"local");
            } else {
                view.showToast("Passwords do not match!");
            }
        }else {
            view.showToast("Please fill in all the fields!");
        }
    }

    @Override
    public void openSignIn() {
        view.signInTvPressed();
    }

    @Override
    public void detachView() {
        view = null;
    }

    private boolean checkIsPasswordsMatch(@NonNull String p1,@NonNull String p2){
        if (p1.equals(p2)){
            return true;
        }
        else {
            return false;
        }
    }

    private boolean isEmpty(List<EditText> list){
        if (list.get(0).getText().toString().isEmpty()){
            return true;
        }else if (list.get(1).getText().toString().isEmpty()){
            return true;
        }else if (list.get(2).getText().toString().isEmpty()){
            return true;
        }else if (list.get(3).getText().toString().isEmpty()){
            return true;
        }else if (list.get(4).getText().toString().isEmpty()){
            return true;
        }else if (list.get(5).getText().toString().isEmpty()){
            return true;
        }else {
            return false;
        }
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

    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(activity, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...
                    }
                });
    }


}
