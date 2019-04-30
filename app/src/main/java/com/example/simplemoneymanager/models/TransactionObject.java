package com.example.simplemoneymanager.models;

import java.util.ArrayList;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class TransactionObject extends RealmObject{

    @PrimaryKey
    private String date;
    private ArrayList<Transaction> allTransactions;
    private int remainingAmount;

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setAllTransactions(ArrayList<Transaction> allTransactions) {
        this.allTransactions = allTransactions;
    }

    public ArrayList<Transaction> getAllTransactions() {
        return allTransactions;
    }

    public void setRemainingAmount(int remainingAmount) {
        this.remainingAmount = remainingAmount;
    }

    public int getRemainingAmount() {
        return remainingAmount;
    }
}
