package com.example.price_analysis_app.Account;

import java.util.ArrayList;

public class SessionManager {
    private static Account currentAccount;


    public static void setCurrentAccount(Account account) {
        currentAccount = new Account(account.getPassword(), account.getUsername(), account.getEmail(), account.getId(), account.getBookmarkedItems());
    }

    public static Account getCurrentAccount() {
        if (currentAccount == null) {
            return new Account("1", "guest", "", "1", new ArrayList<>());
        }
        return currentAccount;
    }
}