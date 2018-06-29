package com.example.mukola.contactapplication.view.acitivities.contact;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mukola.contactapplication.R;
import com.example.mukola.contactapplication.model.models.User;
import com.google.api.services.people.v1.model.Person;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ContactActivity extends AppCompatActivity implements ContactContract.IContactView {

    @BindView(R.id.tv_name_contact)
    TextView name;

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
        if (presenter.checkIsFavorite(person.getString("resourceName"))==true) {

            presenter.deleteFromFavorites(user.getId(),person.getString("resourceName"));
            changeFavoriteSign(R.drawable.ic_star_border_black_24dp);
            presenter.getFavorites(user.getId());


        }else {

            presenter.addToFavorites(user.getId(),person.getString("resourceName"));
            changeFavoriteSign(R.drawable.ic_star_black_24dp);
            presenter.getFavorites(user.getId());

        }
    }

    @NonNull
    private ContactContract.IContactPresenter presenter;

    @NonNull
    private Bundle person;

    @NonNull
    private String pNumber;

    @NonNull
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        ButterKnife.bind(this);

        presenter = new ContactPresenter(this,this,this);

        getData();

        presenter.getFavorites(user.getId());

        initView();
    }


    private void initView(){
        setTitle(getString(R.string.choosen_contact));

        if (presenter.checkIsFavorite(person.getString("resourceName"))==true) {
            changeFavoriteSign(R.drawable.ic_star_black_24dp);
        }

        name.setText(person.getString("name"));

        phone.setText(person.getString("phone"));

        pNumber = person.getString("phone");

        email.setText(person.getString("email"));

        address.setText(person.getString("address"));

        company.setText(person.getString("company"));

        Glide
                .with(this)
                .load(person.getString("url"))
                .into(photo);
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
            person = extras.getBundle("person");
        } }

}
