package com.example.mukola.contactapplication.view.fragments.register;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.EditText;

import com.example.mukola.contactapplication.model.models.User;
import com.example.mukola.contactapplication.model.repositories.GetUserRepository;
import com.example.mukola.contactapplication.model.repositories.GetUserRepositoryImpl;
import com.example.mukola.contactapplication.model.repositories.GoogleSignInRepository;
import com.example.mukola.contactapplication.model.repositories.GoogleSignInRepositoryImpl;
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
    private GoogleSignInRepository googleSignInRepository;

    @NonNull
    private Activity activity;



    public RegisterPresenter (@NonNull RegisterContract.IRegisterView view, @NonNull Context context,@NonNull Activity activity){
        this.view = view;
        this.context = context;
        this.activity = activity;
        registerRepository = new RegisterRepositoryImpl(context);
        getUserRepository = new GetUserRepositoryImpl(context);
        googleSignInRepository = new GoogleSignInRepositoryImpl(activity);

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

    @Override
    public void firebaseAuthWithGoogleR(GoogleSignInAccount account) {
        googleSignInRepository.signIn(account, new GoogleSignInRepository.SignInCallback() {
            @Override
            public void signIn(@NonNull User user) {
                view.openMainScreen(user);
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

    public boolean isOnline() {
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
        googleSignInRepository.logOut();
    }


}
