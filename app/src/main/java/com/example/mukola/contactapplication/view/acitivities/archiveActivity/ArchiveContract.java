package com.example.mukola.contactapplication.view.acitivities.archiveActivity;

import android.support.annotation.NonNull;

import com.example.mukola.contactapplication.model.models.Contact;

import java.util.List;

public interface ArchiveContract {
    public interface IImportView{
        void showToast(@NonNull String message);

        void setContactList(List<Contact> contacts);

        void openContact(@NonNull Contact contact);

        void setTvVisible();
    }

    public interface IImportPresenter{
        void detachView();

        void addToContact(@NonNull int userId,@NonNull Contact contact);

        void openContact(@NonNull Contact contact);

        void getArchive(@NonNull int userId);

        void deleteFromArchive(@NonNull int userId,@NonNull int contactId);
    }
}
