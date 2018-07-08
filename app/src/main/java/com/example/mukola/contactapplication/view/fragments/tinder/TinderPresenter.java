package com.example.mukola.contactapplication.view.fragments.tinder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

import com.example.mukola.contactapplication.R;
import com.example.mukola.contactapplication.model.database.PhotoSaver;
import com.example.mukola.contactapplication.model.models.Contact;
import com.example.mukola.contactapplication.model.repositories.AddToBlacklistRepository;
import com.example.mukola.contactapplication.model.repositories.AddToBlacklistRepositoryImpl;
import com.example.mukola.contactapplication.model.repositories.AddToContactsRepository;
import com.example.mukola.contactapplication.model.repositories.AddToContactsRepositoryImpl;
import com.example.mukola.contactapplication.model.repositories.GetBlacklistRepository;
import com.example.mukola.contactapplication.model.repositories.GetBlacklistRepositoryImpl;

import java.util.ArrayList;

public class TinderPresenter implements TinderContract.ITinderPresenter {

    @NonNull
    private TinderContract.ITinderView view;

    @NonNull
    private Context context;

    @NonNull
    private AddToContactsRepository addToContactsRepository;

    @NonNull
    private AddToBlacklistRepository addToBlacklistRepository;

    @NonNull
    private GetBlacklistRepository getBlacklistRepository;

    @NonNull
    private PhotoSaver photoSaver;

    public TinderPresenter (@NonNull TinderContract.ITinderView view,@NonNull Context context){
        this.view = view;
        this.context = context;
        addToContactsRepository = new AddToContactsRepositoryImpl(context);
        addToBlacklistRepository = new AddToBlacklistRepositoryImpl(context);
        getBlacklistRepository = new GetBlacklistRepositoryImpl(context);
        photoSaver = new PhotoSaver(context);
    }

    @Override
    public void detachView() {
        view = null;
    }

    @Override
    public void initTab(View view) {
        this.view.initTab(view);
    }

    @Override
    public void addToContacts(@NonNull int userId, @NonNull final Contact contact) {
        addToContactsRepository.addToContacts(userId, contact, new AddToContactsRepository.addToContactsCallback() {
            @Override
            public void addedSuccessfull() {
              //  view.showToast(context.getString(R.string.add_successfull));
            }

            @Override
            public void notSuccessfull() {
                view.showToast(context.getString(R.string.error));
            }
        });
    }



    @Override
    public void addToBlackList(@NonNull int userId, @NonNull String contactId) {
        addToBlacklistRepository.addToBlacklist(userId, contactId, new AddToBlacklistRepository.AddToBlacklistCallback() {
            @Override
            public void addedSuccessfull() {
               // view.showToast(context.getString(R.string.deleted_successfully));
            }

            @Override
            public void notSuccessfull() {
                view.showToast(context.getString(R.string.error));
            }
        });
    }

    @Override
    public void getBlackList(@NonNull int userId) {
        getBlacklistRepository.getBlacklist(userId, new GetBlacklistRepository.GetBlacklistCallback() {
            @Override
            public void onBlacklistGet(@NonNull ArrayList<String> list) {
                view.setBlacklist(list);
            }

            @Override
            public void notFound() {
                view.setBlacklist(null);
            }
        });
    }
}
