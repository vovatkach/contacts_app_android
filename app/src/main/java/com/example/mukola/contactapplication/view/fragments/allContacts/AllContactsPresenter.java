package com.example.mukola.contactapplication.view.fragments.allContacts;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;

import com.example.mukola.contactapplication.R;
import com.example.mukola.contactapplication.model.models.Contact;
import com.example.mukola.contactapplication.model.repositories.AddToArchiveRepository;
import com.example.mukola.contactapplication.model.repositories.AddToArchiveRepositoryImpl;
import com.example.mukola.contactapplication.model.repositories.AddToContactsRepository;
import com.example.mukola.contactapplication.model.repositories.AddToContactsRepositoryImpl;
import com.example.mukola.contactapplication.model.repositories.GetBlacklistRepository;
import com.example.mukola.contactapplication.model.repositories.GetBlacklistRepositoryImpl;

import java.util.ArrayList;

public class AllContactsPresenter implements AllContactsContract.IAllContactsPresenter {

    public static final int REQUEST_ID_CALL_PERMISSIONS = 1;

    public static final int REQUEST_ID_SMS_PERMISSIONS = 2;

    @NonNull
    private AllContactsContract.IAllContactsView view;

    @NonNull
    private Activity activity;

    @NonNull
    private AddToContactsRepository addToContactsRepository;

    @NonNull
    private AddToArchiveRepository addToArchiveRepository;

    @NonNull
    private GetBlacklistRepository getBlacklistRepository;

    public AllContactsPresenter (@NonNull AllContactsContract.IAllContactsView view, @NonNull Activity activity,
                                  @NonNull Context context){
        this.view = view;
        this.activity = activity;
        addToContactsRepository = new AddToContactsRepositoryImpl(context);
        addToArchiveRepository = new AddToArchiveRepositoryImpl(context);
        getBlacklistRepository = new GetBlacklistRepositoryImpl(context);
    }



    @Override
    public void detachView() {
        view = null;
    }



    @Override
    public void openContact(@NonNull Contact contact) {
        view.openContact(contact);
    }

    @Override
    public void getBlacklist(@NonNull int userId) {
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


    @Override
    public void addToContact(@NonNull int userId,@NonNull Contact contact){
        addToContactsRepository.addToContacts(userId, contact, new AddToContactsRepository.addToContactsCallback() {
            @Override
            public void addedSuccessfull() {
                view.showToast(activity.getString(R.string.add_successfull));
            }

            @Override
            public void notSuccessfull() {
                view.showToast(activity.getString(R.string.error));
            }
        });
    }

    @Override
    public void addToArchive(@NonNull int userId,@NonNull Contact contact){
        addToArchiveRepository.addToArchive(userId, contact, new AddToArchiveRepository.addToArchiveCallback() {
            @Override
            public void addedSuccessfull() {
                view.showToast(activity.getString(R.string.arc_successfull));
            }

            @Override
            public void notSuccessfull() {
                view.showToast(activity.getString(R.string.error));
            }
        });
    }




}
