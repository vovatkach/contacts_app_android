package com.example.mukola.contactapplication.view.acitivities.contact;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.mukola.contactapplication.R;
import com.example.mukola.contactapplication.model.models.Contact;
import com.example.mukola.contactapplication.model.models.User;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ContactActivity extends AppCompatActivity implements ContactContract.IContactView {

    @BindView(R.id.et_name_contact)
    EditText name;

    @BindView(R.id.et_phone_contact)
    EditText phone;

    @BindView(R.id.et_email_contact)
    EditText email;

    @BindView(R.id.et_address_contact)
    EditText address;

    @BindView(R.id.et_company_contact)
    EditText company;

    @BindView(R.id.profile_image_contact)
    ImageView photo;

    @BindView(R.id.imageView_favorite_contact)
    ImageView star;

    @OnClick(R.id.imageButton_call_contact)
    void onCallClick(View view) {
        presenter.makeCall(pNumber);
    }

    @OnClick(R.id.imageButton_message_contact)
    void onMessageClick(View view) {
        presenter.sendMessage(pNumber);
    }

    @OnClick(R.id.imageView_favorite_contact)
    void onFavoriteClick(View view) {
        if (contact.isFavorite()) {

            presenter.deleteFromFavorites(user.getId(),contact.getId());
            contact.setFavorite(false);
            changeFavoriteSign(R.drawable.ic_star_border_black_24dp);


        }else {

            presenter.addToFavorites(user.getId(),contact.getId());
            contact.setFavorite(true);
            changeFavoriteSign(R.drawable.ic_star_black_24dp);

        }
    }

    @OnClick(R.id.contact_back)
    void onBackClick(View view) {
        onBackPressed();
    }

    @NonNull
    private ContactContract.IContactPresenter presenter;

    @NonNull
    private Contact contact;

    @NonNull
    private String pNumber;

    @NonNull
    private User user;

    private int indicator = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        ButterKnife.bind(this);

        presenter = new ContactPresenter(this,this,this);

        getData();

        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_edit) {
            changeInfo();
            return true;
        }
        if (id == R.id.action_delete) {
            presenter.deleteContact(user.getId(),contact);
            this.finish();
            return true;
        }
        if (id == R.id.action_save) {
            saveInfo();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (indicator==0) {
            menu.findItem(R.id.action_edit).setVisible(false);
            menu.findItem(R.id.action_save).setVisible(true);
        }else {
            menu.findItem(R.id.action_edit).setVisible(true);
            menu.findItem(R.id.action_save).setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }


    private void initView(){

        if (contact.isFavorite()) {
            changeFavoriteSign(R.drawable.ic_star_black_24dp);
        }

        name.setText(contact.getName());

        phone.setText(contact.getNumber());

        pNumber = contact.getNumber();

        email.setText(contact.getEmail());

        address.setText(contact.getAddress());

        company.setText(contact.getCompany());

        if(!contact.getPhotoUrl().equals("null")){
            presenter.getPhoto(contact.getPhotoUrl());
        }else {
            photo.setImageResource(R.drawable.profile);
            name.setTextColor(getResources().getColor(R.color.colorText));
        }
    }


    @Override
    public void makeCall(final String number) {
        pNumber = number;

        if (presenter.checkAndRequestPermissions(ContactPresenter.REQUEST_ID_CALL_PERMISSIONS)) {
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:" + number));
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            startActivity(intent);
        }
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setPhoto(@NonNull Bitmap bitmap) {
        photo.setImageBitmap(bitmap);
    }

    @Override
    public void sendMessage(final String number){
        pNumber = number;

        if(presenter.checkAndRequestPermissions(ContactPresenter.REQUEST_ID_SMS_PERMISSIONS)) {
            // This method will be executed once the timer is over
            // Start your app main activity
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("sms:" + number));
            startActivity(intent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        presenter.onRequestPermissionsResult(requestCode, permissions, grantResults,pNumber);
    }

    private void changeFavoriteSign(int res){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            star.setImageDrawable(getResources().getDrawable(res,
                    getApplicationContext().getTheme()));
        } else {
            star.setImageDrawable(getResources().getDrawable(res));
        }
    }

    private void getData() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            user = (User) extras.getSerializable("user");
            contact = (Contact) extras.getSerializable("contact");
        }
    }

    private   void changeInfo(){
        indicator = 0;
        invalidateOptionsMenu();
        ArrayList<EditText> et = new ArrayList<>();
        et.add(name);
        et.add(phone);
        et.add(email);
        et.add(address);
        et.add(company);

        for (EditText editText: et){
            editText.setClickable(true);
            editText.setFocusable(true);
            editText.setFocusableInTouchMode(true);
            editText.setCursorVisible(true);
        }
    } // змінення даних користувача

    private void saveInfo(){

        indicator = 1;
        invalidateOptionsMenu();

        ArrayList<EditText> et = new ArrayList<>();
        et.add(name);
        et.add(phone);
        et.add(email);
        et.add(address);
        et.add(company);

        for (EditText editText: et){
            editText.setClickable(false);
            editText.setFocusable(false);
            editText.setFocusableInTouchMode(false);
            editText.setCursorVisible(false);
        }

        contact.setName(name.getText().toString());
        contact.setNumber(phone.getText().toString());
        pNumber = phone.getText().toString();
        contact.setEmail(email.getText().toString());
        contact.setAddress(address.getText().toString());
        contact.setCompany(company.getText().toString());

        presenter.editContact(user.getId(),contact);

    }//збереження змін
}
