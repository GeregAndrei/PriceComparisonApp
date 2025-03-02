package com.example.price_analysis_app.Items;

import java.util.List;

public class Account {
    private boolean adminStatus;
    private int id;
    public String username;
    private String email;
    private String password;
    private List<Integer> favorites;

    private boolean isAdminStatus() {
        return adminStatus;
    }

    private void setAdminStatus(boolean adminStatus) {
        this.adminStatus = adminStatus;
    }

    private int getId() {
        return id;
    }

    private void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    private void setUsername(String username) {
        this.username = username;
    }

    private String getEmail() {
        return email;
    }

    private void setEmail(String email) {
        this.email = email;
    }

    private String getPassword() {
        return password;
    }

    private void setPassword(String password) {
        this.password = password;
    }

    public List<Integer> getFavorites() {
        return favorites;
    }

    private void setFavorites(List<Integer> favorites) {
        this.favorites = favorites;
    }

    @Override
    public String toString() {
        return "Account{" +
                "adminStatus=" + adminStatus +
                ", id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", favorites=" + favorites +
                '}';
    }
}
