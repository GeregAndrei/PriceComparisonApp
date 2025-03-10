package com.example.price_analysis_app.Items;

import android.widget.RatingBar;

public class Comment {
    private String accountName;
    private String description;
    private float bar;

    public Comment(String accountName, String description, float bar) {
        this.accountName = accountName;
        this.description = description;
        this.bar = bar;

    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
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
}

