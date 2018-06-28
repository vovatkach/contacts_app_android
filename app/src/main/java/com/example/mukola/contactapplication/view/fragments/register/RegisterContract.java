package com.example.mukola.contactapplication.view.fragments.register;

import android.support.annotation.NonNull;
import android.widget.EditText;

import com.example.mukola.contactapplication.model.models.User;

import java.util.List;

public interface RegisterContract {
    public interface IRegisterView{
        void showToast(@NonNull String message);
        void signInTvPressed();
        void signUpButtonPressed(@NonNull User user);
        void signInGooglePressed();
    }

    public interface IRegisterPresenter{
        void createUser(@NonNull List<EditText> list);
        void openSignIn();
        void signInWithGoogle();
        void detachView();
    }
}
