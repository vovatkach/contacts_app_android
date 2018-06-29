package com.example.mukola.contactapplication.view.acitivities.contact;

public interface ContactContract {
    public interface IContactView{
        void sendMessage(String number);

        void makeCall(String number);

        void showToast(String message);
    }

    public interface IContactPresenter{

        void detachView();

        void sendMessage(String number);

        void makeCall(String number);

        void onRequestPermissionsResult(int requestCode,
                                        String permissions[], int[] grantResults,String number);

        boolean checkAndRequestPermissions(int permissionCode);

        void addToFavorites(int userId,String contactId);

        void getFavorites(int userId);

        boolean checkIsFavorite(String contactId);

        void deleteFromFavorites(int userId,String contactId);
    }
}
