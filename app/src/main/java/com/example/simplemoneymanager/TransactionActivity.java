package com.example.simplemoneymanager;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.example.simplemoneymanager.models.Category;
import com.example.simplemoneymanager.models.Transaction;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class TransactionActivity extends AppCompatActivity {

    Realm realm;
    ArrayList<String> categories;
    ArrayAdapter adapter;
    Spinner cat;
    private String categorySelected;
    private EditText amount;
    private EditText memo;
    private TextView date;
    private Button saveButton;

    private boolean editmode;


    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);
        getSupportActionBar().setTitle("Transaction Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final Intent intent = getIntent();
        editmode = intent.getBooleanExtra("edit_trans", false);
        realm = Realm.getDefaultInstance();
        cat = (Spinner) findViewById(R.id.spinner_category);
        CategoryActivity helper = new CategoryActivity();
        progressBar = findViewById(R.id.progress_bar);
        categories = helper.getCategoryName();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cat.setAdapter(adapter);

        // spinner onclick
        cat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                categorySelected = categories.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        amount = findViewById(R.id.trans_amt);
        date = findViewById(R.id.date);
        Date date2 = Calendar.getInstance().getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = simpleDateFormat.format(date2);
        date.setText(formattedDate);
        memo = findViewById(R.id.memo);
        saveButton = findViewById(R.id.btn_details);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!amount.getText().toString().isEmpty()){
                    if (editmode){
                        Log.i("gertyui", intent.getStringExtra("id"));
                        updateTransaction(intent.getStringExtra("category"), amount.getText().toString(), date.getText().toString(),
                                memo.getText().toString(), intent.getStringExtra("id"));
                    } else {
                        Log.i("kakaka", amount.getText().toString());
                        addTransactionToDatabse(categorySelected, amount.getText().toString(), date.getText().toString(),
                                memo.getText().toString());
                    }
                    finish();
                }
                    //Toast.makeText(this,  "Pl", Toast.LENGTH_SHORT).show()
            }
        });
        if (intent.hasExtra("edit_trans")){
            amount.setText(intent.getStringExtra("amount"));
            date.setText(intent.getStringExtra("date"));
            memo.setText(intent.getStringExtra("memo"));
            cat.setPrompt(intent.getStringExtra("category"));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.trans_del, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.del_trans) {
            deleteTransaction(String.valueOf(id));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void deleteTransaction(final String id){
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<Transaction> realmResults = realm.where(Transaction.class).equalTo("transactionId",
                        id).findAll();realmResults.deleteAllFromRealm();
            }
        });
        Intent in2 = new Intent(TransactionActivity.this, UserActivity.class);
        in2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(in2);
        finish();
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    private void updateTransaction(String category, String amount, String date, String memo, String id){
        deleteTransaction(id);
        String Id = "TRA" + String.valueOf(Calendar.getInstance().getTimeInMillis());
        realm.beginTransaction();
        Transaction transaction = realm.createObject(Transaction.class, Id);
        transaction.setCategory(category);
        transaction.setAmount(amount);
        transaction.setMemo(memo);
        transaction.setDate(date);
        realm.commitTransaction();
    }

    private void addTransactionToDatabse(String category, String amount, String date, String memo){
        progressBar.setVisibility(View.VISIBLE);
        String Id = "TRA" + String.valueOf(Calendar.getInstance().getTimeInMillis());
        realm.beginTransaction();
        Transaction transaction = realm.createObject(Transaction.class, Id);
        transaction.setAmount(amount);
        transaction.setCategory(category);
        transaction.setDate(date);
        transaction.setMemo(memo);
        realm.commitTransaction();
        progressBar.setVisibility(View.GONE);
    }
}
