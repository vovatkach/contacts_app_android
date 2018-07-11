package com.example.mukola.contactapplication.view.acitivities.mainScreen;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.example.mukola.contactapplication.R;
import com.example.mukola.contactapplication.model.database.ModelPreference;
import com.example.mukola.contactapplication.model.models.Contact;
import com.example.mukola.contactapplication.model.models.User;
import com.example.mukola.contactapplication.model.repositories.AddToFavoritesRepository;
import com.example.mukola.contactapplication.model.repositories.AddToFavoritesRepositoryImpl;
import com.example.mukola.contactapplication.model.repositories.DeleteFromFavoritesRepository;
import com.example.mukola.contactapplication.model.repositories.DeleteFromFavoritesRepositoryImpl;
import com.example.mukola.contactapplication.model.repositories.GetContactsRepository;
import com.example.mukola.contactapplication.model.repositories.GetContactsRepositoryImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MSPresenter implements MSContract.IMainScreenPresenter{

    @NonNull
    private MSContract.IMainScreenView view;

    @NonNull
    private Activity activity;


    @NonNull
    private GetContactsRepository getContactsRepository;

    @NonNull
    private AddToFavoritesRepository addToFavoritesRepository;

    @NonNull
    private DeleteFromFavoritesRepository deleteFromFavoritesRepository;

    @NonNull
    private ModelPreference modelPreference;



    public MSPresenter(@NonNull MSContract.IMainScreenView view, @NonNull Activity activity,@NonNull Context context){
        this.view = view;
        this.activity = activity;
        getContactsRepository = new GetContactsRepositoryImpl(context);
        addToFavoritesRepository = new AddToFavoritesRepositoryImpl(context);
        deleteFromFavoritesRepository = new DeleteFromFavoritesRepositoryImpl(context);
        modelPreference = new ModelPreference(context);
    }

    @Override
    public void detachView() {
        view = null;
    }


    @Override
    public void openContact(@NonNull Contact contact, int userId) {
        view.openContact(contact,userId);
    }

    @Override
    public void openCleanUp() {
        view.openCleanUp();
    }

    @Override
    public void openCreateContact() {
        view.openCreateContact();
    }

    @Override
    public void openCityReminder() {
        view.openCityReminder();
    }


    @Override
    public void getContacts(@NonNull int userId) {
        getContactsRepository.getContacts(userId, new GetContactsRepository.GetContactsCallback() {
            @Override
            public void onContactsGet(@NonNull final ArrayList<Contact> list) {

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
                view.setImportButtonVisible();
            }
        });
    }

    @Override
    public void openFavorite() {
        view.openFavorite();
    }

    @Override
    public void addToFavorite(@NonNull int userId, @NonNull Contact contact) {
        addToFavoritesRepository.addToFavorites(userId, contact.getId(), new AddToFavoritesRepository.AddToFavoritesCallback() {
            @Override
            public void addedSuccessfull() {
                view.showToast(activity.getString(R.string.add_to_favorite));
            }

            @Override
            public void notSuccessfull() {
                view.showToast(activity.getString(R.string.error));
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
    public void getHeaderListLatter(@NonNull ArrayList<Contact> usersList, ArrayList<Contact> mSectionList) {

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

    @Override
    public void setFirstStart() {
        modelPreference.firstStartSave(1);
    }

    @Override
    public int checkFirstStart() {
        return modelPreference.firstStartCheck();
    }

    @Override
    public void setUserPreference(User user) {
        modelPreference.saveUserData(user);
    }

}
