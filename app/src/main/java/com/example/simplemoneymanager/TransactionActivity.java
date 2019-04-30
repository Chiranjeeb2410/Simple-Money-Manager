package com.example.simplemoneymanager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.example.simplemoneymanager.models.Category;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import android.widget.Spinner;
import android.widget.Toast;

public class TransactionActivity extends AppCompatActivity {

    Realm realm;
    ArrayList<String> categories;
    ArrayAdapter adapter;
    Spinner cat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);
        getSupportActionBar().setTitle("Transaction Details");

        cat = (Spinner) findViewById(R.id.spinner);

        List<String> cat1 = new ArrayList<>();
        cat1.add("Select Category");

        // retrieve spinner data
        CategoryActivity helper = new CategoryActivity(realm);
        categories = helper.getCategoryName();
        ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, (List) cat1);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cat.setAdapter(adapter);

        // spinner onclick
        cat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(TransactionActivity.this, categories.get(position),
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
