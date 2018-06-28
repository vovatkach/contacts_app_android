package com.example.mukola.contactapplication.model.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;


public class DatabaseHelper extends SQLiteAssetHelper {
        private static final String DATABASE_NAME = "contact_app.db";
        private static final int SCHEMA = 1;

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, SCHEMA);
        }

    }


