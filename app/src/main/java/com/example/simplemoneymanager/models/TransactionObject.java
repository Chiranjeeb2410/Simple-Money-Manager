package com.example.simplemoneymanager.models;

import java.util.ArrayList;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class TransactionObject{

    @PrimaryKey
    private String transactionObjectId;
    private String date;
    private int remainingAmount;

    public void setTransactionObjectId(String transactionObjectId) {
        this.transactionObjectId = transactionObjectId;
    }

    public String getTransactionObjectId() {
        return transactionObjectId;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setRemainingAmount(int remainingAmount) {
        this.remainingAmount = remainingAmount;
    }

    public int getRemainingAmount() {
        return remainingAmount;
    }
}
