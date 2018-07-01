package com.example.mukola.contactapplication.view.acitivities.addContact;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.widget.CheckBox;
import android.widget.EditText;

import com.example.mukola.contactapplication.R;
import com.example.mukola.contactapplication.model.database.PhotoSaver;
import com.example.mukola.contactapplication.model.models.Contact;
import com.example.mukola.contactapplication.model.repositories.AddToContactsRepository;
import com.example.mukola.contactapplication.model.repositories.AddToContactsRepositoryImpl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class AddContactPresenter implements AddContactContract.IContactPresenter {

    @NonNull
    private AddContactContract.IContactView view;

    @NonNull
    private AddToContactsRepository addToContactsRepository;

    @NonNull
    Context context;

    @NonNull Activity activity;

    @NonNull
    PhotoSaver photoSaver;

    private Bitmap bitmap;

    public AddContactPresenter (@NonNull AddContactContract.IContactView view,
                                @NonNull Context context,@NonNull Activity activity){
        this.view = view;
        this.context = context;
        this.activity = activity;
        addToContactsRepository = new AddToContactsRepositoryImpl(context);
        photoSaver = new PhotoSaver(context);
    }

    @Override
    public void createContact(@NonNull List<EditText> list, @NonNull CheckBox checkBox, @NonNull int usedId) {
        Contact contact = new Contact();
        if (!list.get(0).getText().toString().isEmpty()){
            contact.setName(list.get(0).getText().toString());
        }else {
            contact.setName(activity.getString(R.string.no_name));
        }
        if (!list.get(1).getText().toString().isEmpty()){
            contact.setNumber(list.get(1).getText().toString());
        }else {
            contact.setNumber(activity.getString(R.string.no_phone));
        }
        if (!list.get(2).getText().toString().isEmpty()){
            contact.setEmail(list.get(2).getText().toString());
        }else {
            contact.setEmail(activity.getString(R.string.no_email));
        }
        if (!list.get(3).getText().toString().isEmpty()){
            contact.setAddress(list.get(3).getText().toString());
        }else {
            contact.setAddress(activity.getString(R.string.no_address));
        }
        if (!list.get(4).getText().toString().isEmpty()){
            contact.setCompany(list.get(4).getText().toString());
        }else {
            contact.setCompany(activity.getString(R.string.no_company));
        }

        if (checkBox.isChecked()){
            contact.setFavorite(true);
        }else {
            contact.setFavorite(false);
        }

        if (bitmap!=null){
            contact.setPhotoUrl(saveToInternalStorage(bitmap));
        }else{
            contact.setPhotoUrl("null");
        }

        addToContact(contact,usedId);

    }


    private  void addToContact(@NonNull Contact contact,@NonNull int userId){
        addToContactsRepository.addToContacts(userId, contact, new AddToContactsRepository.addToContactsCallback() {
            @Override
            public void addedSuccessfull() {
                activity.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        view.showToast("Contact added succesfully");
                        view.stop();
                    }
                });

            }

            @Override
            public void notSuccessfull() {

                activity.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        view.showToast("Contact added succesfully");
                        view.setProgressBarGone();
                    }
                });
            }
        });
    }


    private String saveToInternalStorage(Bitmap bitmapImage){
       return photoSaver.saveToInternalStorage(bitmapImage);
    }

    @Override
    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    @Override
    public void choosePhoto() {
        view.getPhoto();
    }


    @Override
    public void detachView() {
        view = null;
    }
}
