package com.example.mukola.contactapplication.view.acitivities.favorite;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.example.mukola.contactapplication.R;
import com.example.mukola.contactapplication.model.models.Contact;
import com.example.mukola.contactapplication.model.repositories.AddToFavoritesRepository;
import com.example.mukola.contactapplication.model.repositories.AddToFavoritesRepositoryImpl;
import com.example.mukola.contactapplication.model.repositories.DeleteFromFavoritesRepository;
import com.example.mukola.contactapplication.model.repositories.DeleteFromFavoritesRepositoryImpl;
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

    @NonNull
    private AddToFavoritesRepository addToFavoritesRepository;

    @NonNull
    private DeleteFromFavoritesRepository deleteFromFavoritesRepository;

    @NonNull
    private Context context;


    public FavoritePresenter (@NonNull FavoritContract.IFavoriteView view ,@NonNull Context context){

        this.view = view;
        this.context= context;
        getFavoritesRepository = new GetFavoritesRepositoryImpl(context);
        addToFavoritesRepository = new AddToFavoritesRepositoryImpl(context);
        deleteFromFavoritesRepository = new DeleteFromFavoritesRepositoryImpl(context);

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

    @Override
    public void addToFavorite(@NonNull int userId, @NonNull Contact contact) {
        addToFavoritesRepository.addToFavorites(userId, contact.getId(), new AddToFavoritesRepository.AddToFavoritesCallback() {
            @Override
            public void addedSuccessfull() {
                view.showToast(context.getString(R.string.add_to_favorite));
            }

            @Override
            public void notSuccessfull() {
                view.showToast(context.getString(R.string.error));
            }
        });
    }

    @Override
    public void deleteFRomFavorite(@NonNull int userId, @NonNull Contact contact) {
        deleteFromFavoritesRepository.deleteFromFavorites(userId, contact.getId(), new DeleteFromFavoritesRepository.deleteFromFavoritesCallback() {
            @Override
            public void deletedSuccessfull() {
                Log.d("MS DELETED","FROM FAVORITE");
            }

            @Override
            public void notSuccessfull() {
                Log.d("MS NOT DELETED","FROM FAVORITE");
            }
        });
    }

    @Override
    public void getHeaderListLatter(ArrayList<Contact> usersList,ArrayList<Contact> mSectionList) {

        Collections.sort(usersList, new Comparator<Contact>() {
            @Override
            public int compare(Contact user1, Contact user2) {
                return String.valueOf(user1.getName().charAt(0)).toUpperCase()
                        .compareTo(String.valueOf(user2.getName().charAt(0)).toUpperCase());
            }
        });

        String lastHeader = "";

        int size = usersList.size();

        for (int i = 0; i < size; i++) {

            Contact user = usersList.get(i);
            String header = String.valueOf(user.getName().charAt(0)).toUpperCase();

            if (!TextUtils.equals(lastHeader, header)) {
                lastHeader = header;

                mSectionList.add(new Contact(header,true));
            }

            mSectionList.add(user);
        }
    }
}
