package com.example.mukola.contactapplication.model.database;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.mukola.contactapplication.model.models.User;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class ModelPreference {

    SharedPreferences settings;

    public ModelPreference(Context context){
        settings = context.getSharedPreferences(PREFS_FILE, MODE_PRIVATE);
    }

    private static final String PREFS_FILE = "Account";
    private static final String IS_FIRST = "isFirst";
    private static final String LOGIN = "user_login";
    private static final String PASSWORD = "user_password";
    private static final String NUMBER = "login";
    private static final String ADDRESS = "address";
    private static final String ID = "id";
    private static final String NAME = "name";


    public void saveUserData(User user) { // функція зберегти пароль
        SharedPreferences.Editor prefEditor = settings.edit();
        prefEditor.putString(LOGIN, user.getEmail());
        prefEditor.putString(PASSWORD, user.getPassword());
        prefEditor.putString(ADDRESS, user.getAddress());
        prefEditor.putString(NUMBER, user.getNumber());
        prefEditor.putInt(ID, user.getId());
        prefEditor.putString(NAME, user.getName());

        prefEditor.apply();
    }

    public User getUserData() {//функція витягнування паролю при включенні додатку

        User user = new User();
        user.setId(settings.getInt(ID,-1));
        user.setName(settings.getString(NAME,""));
        user.setEmail(settings.getString(LOGIN,""));
        user.setPassword(settings.getString(PASSWORD,""));
        user.setNumber(settings.getString(NUMBER,""));
        user.setAddress(settings.getString(ADDRESS,""));

        return user;
    }

    public void firstStartSave(int x) { // функція зберегти перший запуск прогами
        SharedPreferences.Editor prefEditor = settings.edit();
        prefEditor.putInt(IS_FIRST, x);
        prefEditor.apply();
    }

    public int firstStartCheck() {//функція визначення чи програма стратує вперше
        int x = settings.getInt(IS_FIRST,0);
        return x;
    }


}
