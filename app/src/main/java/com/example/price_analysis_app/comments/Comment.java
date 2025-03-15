package com.example.price_analysis_app.comments;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Comment implements Parcelable {
    private String accountName;

    private String productId;
    private String description;
    private float bar;

    public Comment(String accountName, String productId, String description, float bar) {
        this.accountName = accountName;
        this.productId = productId;
        this.description = description;
        this.bar = bar;
    }
    protected Comment(Parcel in) {
        accountName = in.readString();
        productId = in.readString();
        description = in.readString();
        bar = in.readFloat();
    }
    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getBar() {
        return bar;
    }

    public void setBar(float bar) {
        this.bar = bar;
    }

    public static final Creator<Comment> CREATOR = new Creator<Comment>() {
        @Override
        public Comment createFromParcel(Parcel in) {
            return new Comment(in);
        }

        @Override
        public Comment[] newArray(int size) {
            return new Comment[size];
        }
    };
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int flags) {
        parcel.writeString(accountName);
        parcel.writeString(productId);
        parcel.writeString(description);
        parcel.writeFloat(bar);
    }

    @Override
    public String toString() {
        return "Comment{" +
                "accountName='" + accountName + '\'' +
                ", description='" + description + '\'' +
                ", bar=" + bar +
                '}';
    }
}

