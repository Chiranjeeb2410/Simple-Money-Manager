package com.example.simplemoneymanager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class TransactionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);
        getSupportActionBar().setTitle("Transaction Details");
    }
}
