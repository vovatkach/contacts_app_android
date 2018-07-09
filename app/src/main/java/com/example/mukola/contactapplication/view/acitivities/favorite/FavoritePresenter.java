package com.example.mukola.contactapplication.view.acitivities.favorite;

import android.content.Context;
import android.support.annotation.NonNull;

import com.example.mukola.contactapplication.model.models.Contact;
import com.example.mukola.contactapplication.model.repositories.GetFavoritesRepository;
import com.example.mukola.contactapplication.model.repositories.GetFavoritesRepositoryImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class FavoritePresenter implements FavoritContract.IFavoritePresenter{

    @NonNull
    private FavoritContract.IFavoriteView view;

    @NonNull
    private GetFavoritesRepository getFavoritesRepository;

    public FavoritePresenter (@NonNull FavoritContract.IFavoriteView view ,@NonNull Context context){

        this.view = view;
        getFavoritesRepository = new GetFavoritesRepositoryImpl(context);

    }

    @Override
    public void detachView() {
        view = null;
    }

    @Override
    public void onContactClicked(@NonNull Contact contact) {
        view.onContactClicked(contact);
    }

    @Override
    public void getFavorites(int userId) {
        getFavoritesRepository.getFavorites(userId, new GetFavoritesRepository.GetFavoritesCallback() {
            @Override
            public void onFavoritesGet(@NonNull final ArrayList<Contact> list) {
                Collections.sort(list, new Comparator<Contact>() {
                    @Override
                    public int compare(Contact contact2, Contact contact1)
                    {

                        return  contact2.getName().compareTo(contact1.getName());
                    }
                });
                view.setContactList(list);
            }

            @Override
            public void notFound() {
                view.setvNoFavoriteVisible();
            }
        });
    }

    @Override
    public void openContact(@NonNull Contact contact, int userId) {
        view.openContact(contact, userId);
    }

}
