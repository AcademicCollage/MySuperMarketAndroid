package com.example.mySuperMarket.objects;

import java.util.ArrayList;

public class User {
    private String userName;
    private String password;
    private ArrayList<String>ids;

    public User(String userName, String password, ArrayList<String> ids) {
        this.userName = userName;
        this.password = password;
        this.ids = ids;
    }

    public User() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ArrayList<String> getIds() {
        return ids;
    }

    public void setIds(ArrayList<String> ids) {
        this.ids = ids;
    }
}
