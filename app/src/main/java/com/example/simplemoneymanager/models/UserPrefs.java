package com.example.simplemoneymanager.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class UserPrefs extends RealmObject{

    @PrimaryKey
    private String monthly_limit;
    private String todaysExpenses;
    private String balance;

    public void setMonthly_limit(String monthly_limit) {
        this.monthly_limit = monthly_limit;
    }

    public String getMonthly_limit() {
        return monthly_limit;
    }

    public void setTodaysExpenses(String todaysExpenses) {
        this.todaysExpenses = todaysExpenses;
    }

    public String getTodaysExpenses() {
        return todaysExpenses;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getBalance() {
        return balance;
    }
}
