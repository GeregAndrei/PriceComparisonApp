package com.example.price_analysis_app.Account;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class Account implements Parcelable {
    private boolean adminStatus;
    private String id;
    public String username;
    private String email;
    private String password;
    public List<String> bookmarkedItems=new ArrayList<>();
    public Account() {

        adminStatus=false;
        id="";
        username="";
        email="";
        password="";
        bookmarkedItems=new ArrayList<>();
    }

    public Account(String password, String username, String email, String id, List<String> bookmarkedItems) {
        this.password = password;
        this.username = username;
        this.email = email;
        this.id = id;
        this.bookmarkedItems = bookmarkedItems;
    }

    protected Account(Parcel in) {
        adminStatus = in.readByte() != 0;
        id = in.readString();
        username = in.readString();
        email = in.readString();
        password = in.readString();
        bookmarkedItems = in.createStringArrayList();
    }


    public static final Creator<Account> CREATOR = new Creator<Account>() {
        @Override
        public Account createFromParcel(Parcel in) {
            return new Account(in);
        }

        @Override
        public Account[] newArray(int size) {
            return new Account[size];
        }
    };

    public boolean isAdminStatus() {
        return adminStatus;
    }

    public void setAdminStatus(boolean adminStatus) {
        this.adminStatus = adminStatus;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<String> getBookmarkedItems() {
        if(bookmarkedItems==null){
           this.bookmarkedItems=new ArrayList<>();
           return bookmarkedItems;
        }
        return bookmarkedItems;
    }

    public void setBookmarkedItems(List<String> bookmarkedItems) {
        this.bookmarkedItems = bookmarkedItems;
    }

    @Override
    public String toString() {
        return "Account{" +
                "adminStatus=" + adminStatus +
                ", id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", favorites=" + bookmarkedItems.toString() +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeByte((byte) (adminStatus ? 1 : 0));
        parcel.writeString(id);
        parcel.writeString(username);
        parcel.writeString(email);
        parcel.writeString(password);
        parcel.writeStringList(bookmarkedItems);
    }
}
