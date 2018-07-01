package com.example.mukola.contactapplication.view.acitivities.addContact;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mukola.contactapplication.R;
import com.example.mukola.contactapplication.model.models.User;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddContactActivity extends AppCompatActivity implements AddContactContract.IContactView {

    @BindViews({R.id.et_name_new,R.id.et_phone_new,R.id.et_email_new,R.id.et_address_new,R.id.et_company_new})
    List<EditText> etList;

    @BindViews({R.id.textView_name_new,R.id.textView_numb_new,R.id.textView_email_new,R.id.textView_address_new,
            R.id.textView_comany_new,R.id.textView_add_photo_new})
    List<TextView> tvList;

    @BindView(R.id.checkBox_is_fav_new)
    CheckBox isFavorite;

    @BindView(R.id.profile_image_new)
    ImageView photo;

    @BindView(R.id.progressBar_new)
    ProgressBar progressBar;

    @BindView(R.id.button_new_contact)
    Button button;

    @OnClick(R.id.profile_image_new)
    void onAddPhotoClick(View view) {
        presenter.choosePhoto();
    }

    @OnClick(R.id.button_new_contact)
    void onCreateClick(View view) {
        setProgressBarVisible();
        new Thread(new Runnable() {
            @Override
            public void run() {
                presenter.createContact(etList,isFavorite,user.getId());
            }
        }).start();
        }

    private static final int GALLERY_REQUEST = 1;

    @NonNull
    private AddContactContract.IContactPresenter presenter;

    @NonNull
    private User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
        ButterKnife.bind(this);

        presenter = new AddContactPresenter(this,this,this);

        getData();


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK)
            switch (requestCode){
                case GALLERY_REQUEST:
                    Uri selectedImage = data.getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                        photo.setImageBitmap(bitmap);
                        presenter.setBitmap(bitmap);
                    } catch (IOException e) {
                        Log.i("TAG", "Some exception " + e);
                    }
                    break;
            }
    }

    private void getData() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            user = (User) extras.getSerializable("user");
        }
    }

    @Override
    public void showToast(@NonNull String message) {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void getPhoto() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
    }

    @Override
    public void setProgressBarVisible() {
        photo.setVisibility(View.INVISIBLE);
        isFavorite.setVisibility(View.INVISIBLE);
        for (EditText t:etList) {
            t.setVisibility(View.INVISIBLE);
        }
        for (TextView v:tvList) {
            v.setVisibility(View.INVISIBLE);
        }
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void stop() {
        this.finish();
    }

    @Override
    public void setProgressBarGone() {
        progressBar.setVisibility(View.INVISIBLE);
        photo.setVisibility(View.VISIBLE);
        isFavorite.setVisibility(View.VISIBLE);
        for (EditText t:etList) {
            t.setVisibility(View.VISIBLE);
        }
        for (TextView v:tvList) {
            v.setVisibility(View.VISIBLE);
        }

    }

}
