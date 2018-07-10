package com.example.mukola.contactapplication.model.models;

import java.io.Serializable;

public class Contact implements Serializable {

    private int id;

    private String name;

    private String number;

    private String email;

    private String address;

    private String company;

    private String photoUrl;

    private boolean isFavorite;

    private String blacklistId;

    private boolean isSectioned;

    public Contact(int id, String name, String number, String email,
                   String address, String company, String photoUrl,String blacklistId){
        this.id = id;
        this.name = name;
        this.number = number;
        this.email = email;
        this.address = address;
        this.company = company;
        this.photoUrl = photoUrl;
        this.blacklistId = blacklistId;
    }

    public Contact(String name,boolean isSectioned){
        this.name = name;
        this.isSectioned = isSectioned;
        this.number = "";
        this.email = "";
        this.address = "";
        this.company = "";
        this.photoUrl = "";
        this.blacklistId = "";
    }

    public Contact(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public String getBlacklistId() {
        return blacklistId;
    }

    public void setBlacklistId(String blacklistId) {
        this.blacklistId = blacklistId;
    }

    public boolean isSectioned() {
        return isSectioned;
    }

    public void setSectioned(boolean sectioned) {
        isSectioned = sectioned;
    }
}
