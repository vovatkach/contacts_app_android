package com.example.mukola.contactapplication.view.fragments.login;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.mukola.contactapplication.R;
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

public class LoginPresenter implements LoginContract.IRegisterPresenter {

    @NonNull
    private LoginContract.IRegisterView view;

    @NonNull
    private Context context;

    @NonNull
    private GetUserRepository getUserRepository;

    @NonNull
    private RegisterRepository registerRepository;


    public LoginPresenter (LoginContract.IRegisterView view,@NonNull Context context){
        this.view = view;
        this.context = context;
        getUserRepository = new GetUserRepositoryImpl(context);
        registerRepository = new RegisterRepositoryImpl(context);
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




    @Override
    public void createGoogleUser(@NonNull String email) {
        User user = new User();
        user.setName(email);
        user.setNumber(context.getString(R.string.no_phone));
        user.setAddress(context.getString(R.string.no_address));
        user.setEmail(email);
        user.setPassword(new StringBuilder(email).reverse().toString());
        register(user);
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



}
