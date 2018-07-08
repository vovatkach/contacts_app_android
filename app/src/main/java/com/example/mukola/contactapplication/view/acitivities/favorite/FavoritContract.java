package com.example.mukola.contactapplication.view.acitivities.favorite;

import android.support.annotation.NonNull;

import com.example.mukola.contactapplication.model.models.Contact;

import java.util.List;

public interface FavoritContract {
    public interface IFavoriteView{

        void onContactClicked(@NonNull Contact contact);

        void setContactList(List<Contact> contacts);

        void showToast(@NonNull String message);

        void setvNoFavoriteVisible();

        void openContact(@NonNull Contact contact, int userId);

    }

    public interface IFavoritePresenter{

        void detachView();

        void onContactClicked(@NonNull Contact contact);

        void getFavorites(@NonNull int userId);

        void openContact(@NonNull Contact contact, int userId);
    }
}
