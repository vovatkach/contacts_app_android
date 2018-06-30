package com.example.mukola.contactapplication.view.acitivities.main;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.mukola.contactapplication.R;
import com.example.mukola.contactapplication.model.models.User;
import com.example.mukola.contactapplication.view.acitivities.mainScreen.MainScreenActivity;
import com.example.mukola.contactapplication.view.fragments.login.LoginFragment;
import com.example.mukola.contactapplication.view.fragments.register.RegisterFragment;
import com.example.mukola.contactapplication.view.fragments.start.StartFragment;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.Task;

import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MainContract.IMainView, LoginFragment.OnLoginFragmentInteractionListener,
        RegisterFragment.OnRegisterFragmentInteractionListener,StartFragment.OnStartFragmentInteractionListener
{

    @Nullable
    MainContract.IMainPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        presenter = new MainPresenter(this,this);
        presenter.openStartFragment();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    @Override
    public void openLoginFragment() {
        LoginFragment amf = LoginFragment.newInstance();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content, amf)
                .addToBackStack("LoginFragment")
                .commit();
    }

    @Override
    public void openRegisterFragment() {
        RegisterFragment amf = RegisterFragment.newInstance();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content, amf)
                .addToBackStack("RegisterFragment")
                .commit();
    }


    @Override
    public void openMainScreen(@NonNull User user) {
        Intent intent = new Intent(this, MainScreenActivity.class);
        intent.putExtra("user",user);
        startActivity(intent);
        this.finish();
    }

    @Override
    public void openStartFragment() {
        StartFragment sf = new StartFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.content, sf)
                .commit();
    }

    @Override
    public void showToast(@NonNull String message) {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSignUpTVPressed() {
        presenter.openRegisterFragment();
    }

    @Override
    public void openMainScreenFromLoginFragment(@NonNull User user) {
        presenter.openMainScreen(user);
    }

    @Override
    public void onSignInTvPressed() {
        presenter.openLoginFragment();
    }

    @Override
    public void openMainScreenFromRegisterFragment(@NonNull User user) {
        presenter.openMainScreen(user);
    }

    @Override
    public void onLoginClick() {
        presenter.openLoginFragment();
    }

    @Override
    public void onRegisterClick() {
        presenter.openRegisterFragment();
    }

}
