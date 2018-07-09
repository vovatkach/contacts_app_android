package com.example.mukola.contactapplication.view.fragments.register;

import android.support.annotation.NonNull;
import android.widget.EditText;

import com.example.mukola.contactapplication.model.models.User;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.Task;

import java.util.List;

public interface RegisterContract {
    public interface IRegisterView{
        void showToast(@NonNull String message);
        void signInTvPressed();
        void openMainScreen(@NonNull User user);
    }

    public interface IRegisterPresenter{
        void createUser(@NonNull List<EditText> list);
        void openSignIn();
        void detachView();
        void register(@NonNull final User user,String type);
        void firebaseAuthWithGoogleR(GoogleSignInAccount account);
        boolean isOnline();

    }
}
