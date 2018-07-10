package com.example.mukola.contactapplication.view.acitivities.archiveActivity;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.example.mukola.contactapplication.R;
import com.example.mukola.contactapplication.model.models.Contact;
import com.example.mukola.contactapplication.model.repositories.AddToContactsRepository;
import com.example.mukola.contactapplication.model.repositories.AddToContactsRepositoryImpl;
import com.example.mukola.contactapplication.model.repositories.DeleteFromArchiveRepository;
import com.example.mukola.contactapplication.model.repositories.DeleteFromArchiveRepositoryImpl;
import com.example.mukola.contactapplication.model.repositories.GetArchiveRepository;
import com.example.mukola.contactapplication.model.repositories.GetArchiveRepositoryImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ArchivePresenter implements ArchiveContract.IImportPresenter {

    @NonNull
    private ArchiveContract.IImportView view;


    @NonNull
    private Activity activity;

    @NonNull
    private AddToContactsRepository addToContactsRepository;

    @NonNull
    private GetArchiveRepository getArchiveRepository;

    @NonNull
    private DeleteFromArchiveRepository deleteFromArchiveRepository;


    public ArchivePresenter(@NonNull ArchiveContract.IImportView view, @NonNull Activity activity, @NonNull Context context){
        this.view = view;
        this.activity = activity;
        addToContactsRepository = new AddToContactsRepositoryImpl(context);
        getArchiveRepository = new GetArchiveRepositoryImpl(context);
        deleteFromArchiveRepository = new DeleteFromArchiveRepositoryImpl(context);
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
    public void getArchive(@NonNull int userId) {
        getArchiveRepository.getArchive(userId, new GetArchiveRepository.GetArchiveCallback() {
            @Override
            public void onArchiveGet(@NonNull ArrayList<Contact> list) {
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
                view.setTvVisible();
            }
        });
    }

    @Override
    public void deleteFromArchive(@NonNull final int userId, @NonNull int contactId) {
        deleteFromArchiveRepository.deleteFromArchive(userId, contactId, new DeleteFromArchiveRepository.DeleteFromArchiveCallback() {
            @Override
            public void deletedSuccessfull() {
                getArchive(userId);
            }

            @Override
            public void notSuccessfull() {
                Log.d("Archive","NOT DELETED");
            }
        });
    }


    @Override
    public void addToContact(@NonNull int userId, @NonNull Contact contact){
        addToContactsRepository.addToContacts(userId, contact, new AddToContactsRepository.addToContactsCallback() {
            @Override
            public void addedSuccessfull() {
                view.showToast(activity.getString(R.string.backed_succ));
            }

            @Override
            public void notSuccessfull() {
                view.showToast(activity.getString(R.string.error));
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
