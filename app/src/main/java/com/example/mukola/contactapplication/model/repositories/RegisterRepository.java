package com.example.mukola.contactapplication.model.repositories;

import android.support.annotation.NonNull;

import com.example.mukola.contactapplication.model.models.User;

public interface RegisterRepository {
    public void register(@NonNull User user, @NonNull RegisterRepository.RegisterCallback registerCallback);

    public interface RegisterCallback {
        void register(@NonNull User user);
    }
}
