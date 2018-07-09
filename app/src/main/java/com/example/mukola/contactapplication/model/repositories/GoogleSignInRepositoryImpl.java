package com.example.mukola.contactapplication.model.repositories;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.mukola.contactapplication.model.models.User;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import static android.support.constraint.Constraints.TAG;

public class GoogleSignInRepositoryImpl implements GoogleSignInRepository {

    private FirebaseAuth mAuth;

    private Activity context;


    public GoogleSignInRepositoryImpl(Activity context){
        this.context = context;
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void signIn(@NonNull GoogleSignInAccount acct, @NonNull final SignInCallback Callback) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener( context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            User u = new User();
                            u.setName(user.getDisplayName());
                            u.setEmail(user.getEmail());
                            u.setAddress("No address");
                            u.setPassword(user.getUid());
                            u.setNumber(user.getPhoneNumber());
                            Callback.signIn(u);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                        }
                    }
                });
    }

    @Override
    public void logOut(){
        mAuth.signOut();
    }
}
