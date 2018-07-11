package com.example.mukola.contactapplication.view.fragments.tinder;

import android.support.annotation.NonNull;
import android.view.View;

import com.example.mukola.contactapplication.model.models.Contact;

import java.util.ArrayList;

public interface TinderContract {
    interface ITinderView{

        void showToast(@NonNull String message);

        void initTab(View view);

        void setBlacklist(@NonNull ArrayList<String> blacklist);


    }

    interface ITinderPresenter{

        void detachView();

        void initTab(View view);

        void addToContacts(@NonNull int userId,@NonNull Contact contact);

        void addToBlackList(@NonNull int userId, @NonNull String contactId);

        void getBlackList(@NonNull int userId);

        void addToArchive(@NonNull int userId,@NonNull Contact contact);


    }
}
