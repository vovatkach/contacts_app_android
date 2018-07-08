package com.example.mukola.contactapplication.view.acitivities.main;

import android.support.annotation.NonNull;

import com.example.mukola.contactapplication.model.models.User;

public interface MainContract {
    public interface IMainView{
        void openLoginFragment();
        void openRegisterFragment();
        void showToast(@NonNull String message);
        void openMainScreen(@NonNull User user);
        void openStartFragment();
    }

    public interface IMainPresenter{
        void openLoginFragment();
        void openRegisterFragment();
        void openMainScreen(@NonNull User user);
        void detachView();
        void openStartFragment();
    }
}
