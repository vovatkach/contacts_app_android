package com.example.mukola.contactapplication.model.database;

public class DatabaseContract {

    public static class USERS {
        public static  String TABLE = "users";
        public static  String COLUMN_ID = "id";
        public static  String COLUMN_NAME = "name";
        public static  String COLUMN_PASSWORD = "password";
        public static  String COLUMN_NUMBER = "number";
        public static  String COLUMN_ADDRESS = "address";
        public static  String COLUMN_EMAIL = "email";
    }

    public static class FAVORITES {
        public static  String TABLE = "favorites";
        public static  String COLUMN_ID = "id";
        public static  String COLUMN_USER_ID = "userId";
        public static  String COLUMN_CONTACT_ID = "contactId";
    }



}
