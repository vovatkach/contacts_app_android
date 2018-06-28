package com.example.mukola.contactapplication.view.fragments.register;

import android.support.annotation.NonNull;
import android.widget.EditText;

import com.example.mukola.contactapplication.model.models.User;

import java.util.List;

public class RegisterPresenter implements RegisterContract.IRegisterPresenter {
    private RegisterContract.IRegisterView view;

    public RegisterPresenter (@NonNull RegisterContract.IRegisterView view){
        this.view = view;
    }

    @Override
    public void createUser(@NonNull List<EditText> list){
        if (isEmpty(list)==false) {

            if (checkIsPasswordsMatch(list.get(4).getText().toString(),
                    list.get(5).getText().toString()) == true) {
                User user = new User();
                user.setName(list.get(0).getText().toString());
                user.setNumber(list.get(1).getText().toString());
                user.setAddress(list.get(2).getText().toString());
                user.setEmail(list.get(3).getText().toString());
                user.setPassword(list.get(4).getText().toString());
                view.signUpButtonPressed(user);
            } else {
                view.showToast("Passwords do not match!");
            }
        }else {
            view.showToast("Please fill in all the fields!");
        }
    }

    @Override
    public void openSignIn() {
        view.signInTvPressed();
    }

    @Override
    public void signInWithGoogle() {
        view.signInGooglePressed();
    }

    @Override
    public void detachView() {
        view = null;
    }

    private boolean checkIsPasswordsMatch(@NonNull String p1,@NonNull String p2){
        if (p1.equals(p2)){
            return true;
        }
        else {
            return false;
        }
    }

    private boolean isEmpty(List<EditText> list){
        if (list.get(0).getText().toString().isEmpty()){
            return true;
        }else if (list.get(1).getText().toString().isEmpty()){
            return true;
        }else if (list.get(2).getText().toString().isEmpty()){
            return true;
        }else if (list.get(3).getText().toString().isEmpty()){
            return true;
        }else if (list.get(4).getText().toString().isEmpty()){
            return true;
        }else if (list.get(5).getText().toString().isEmpty()){
            return true;
        }else {
            return false;
        }
    }
}
