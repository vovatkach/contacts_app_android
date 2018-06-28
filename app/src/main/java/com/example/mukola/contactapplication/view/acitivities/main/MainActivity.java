package com.example.mukola.contactapplication.view.acitivities.main;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.mukola.contactapplication.R;
import com.example.mukola.contactapplication.model.models.User;
import com.example.mukola.contactapplication.view.acitivities.mainScreen.MainScreenActivity;
import com.example.mukola.contactapplication.view.fragments.login.LoginFragment;
import com.example.mukola.contactapplication.view.fragments.register.RegisterFragment;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.Task;

import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MainContract.IMainView, LoginFragment.OnLoginFragmentInteractionListener,
        RegisterFragment.OnRegisterFragmentInteractionListener
{

    private static final int RC_SIGN_IN = 1;

    @Nullable
    MainContract.IMainPresenter presenter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        presenter = new MainPresenter(this,this);
        presenter.openLoginFragment();
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
    public void signIn() {
        Intent signInIntent = presenter.getGoogleSignInClient().getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void openMainScreen(@NonNull User user) {
        Intent intent = new Intent(this, MainScreenActivity.class);
        intent.putExtra("user",user);
        startActivity(intent);
        this.finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            presenter.handleSignInResult(task);
        }
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
    public void onLoginBtnPressed(@NonNull String email, @NonNull String password) {
        presenter.login(email,password);
    }

    @Override
    public void onGoogleSignInPressed() {
        presenter.signIn();
    }

    @Override
    public void onSignInTvPressed() {
        presenter.openLoginFragment();
    }

    @Override
    public void onSignUpBtnPressed(@NonNull User user) {
        presenter.register(user);
    }

    @Override
    public void onGoogleSignUpPressed() {
        presenter.signIn();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }
}
