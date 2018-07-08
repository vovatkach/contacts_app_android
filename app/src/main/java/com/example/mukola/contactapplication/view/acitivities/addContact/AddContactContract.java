package com.example.mukola.contactapplication.view.acitivities.addContact;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.widget.CheckBox;
import android.widget.EditText;

import java.util.List;

public interface AddContactContract {

    public interface IContactView{

        void showToast(@NonNull String message);

        void getPhoto();

        void setProgressBarVisible();

        void stop();

        void setProgressBarGone();

    }

    public interface IContactPresenter{

        void detachView();

        void createContact(@NonNull List<EditText> list, @NonNull CheckBox checkBox, @NonNull int userId);

        void choosePhoto();


        void setBitmap(Bitmap bitmap);

    }
}
