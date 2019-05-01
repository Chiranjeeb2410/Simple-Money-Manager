package com.example.simplemoneymanager;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.usb.UsbRequest;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.simplemoneymanager.adapters.TransactionAdapter;
import com.example.simplemoneymanager.models.Category;
import com.example.simplemoneymanager.models.Transaction;
import com.google.firebase.auth.FirebaseAuth;
import com.jaeger.library.StatusBarUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import io.realm.Realm;
import io.realm.RealmResults;

public class UserActivity extends AppCompatActivity {
    Button btnLogOut;
    FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FloatingActionButton floatingActionButton;

    private RecyclerView recyclerView;

    private Realm realm;

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        Realm.init(getApplicationContext());
        realm = Realm.getDefaultInstance();
        btnLogOut = (Button) findViewById(R.id.btnLogOut);
        recyclerView = findViewById(R.id.expense_item_view);
        Date date2 = Calendar.getInstance().getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = simpleDateFormat.format(date2);
        getSupportActionBar().setTitle("Today, "+formattedDate);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        setAdapter();
        floatingActionButton = findViewById(R.id.add_floating_button);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(UserActivity.this);
                builder.setItems(getResources().getStringArray(R.array.add_array), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i){
                            case 0:
                                AlertDialog.Builder builder1 = new AlertDialog.Builder(UserActivity.this);
                                View view1 = getLayoutInflater().inflate(R.layout.dialog_add_category, null);
                                final EditText categoryName = view1.findViewById(R.id.category_name);
                                final EditText categoryLimit = view1.findViewById(R.id.category_limit);
                                builder1.setView(view1);
                                builder1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        if (categoryName.getText().toString().isEmpty() || categoryLimit.getText().toString().isEmpty()){
                                            Toast.makeText(getApplicationContext(), "Enter valid info for new category", Toast.LENGTH_SHORT).show();
                                        } else {
                                            String category = categoryName.getText().toString();
                                            String categoryId = category.substring(0, 3).toUpperCase();
                                            Random random = new Random();
                                            String randomno = String.valueOf(random.nextInt(99));
                                            String categoryType = categoryLimit.getText().toString();
                                            dialogInterface.dismiss();
                                            addCategoryInfo(categoryId+randomno, category, categoryType);
                                        }
                                        Intent intent = new Intent(UserActivity.this, CategoryActivity.class);
                                        startActivity(intent);
                                    }
                                });

                                builder1.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                });
                                AlertDialog dialog = builder1.create();
                                dialog.show();

                                break;

                            case 1:
                                Intent in = new Intent(UserActivity.this, TransactionActivity.class );
                                startActivity(in);
                                break;
                        }

                    }
                });
                Dialog dialog = builder.create();
                dialog.show();
            }
        });

        // logout user through firebaseAuth
        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent I = new Intent(UserActivity.this, LoginActivity.class);
                startActivity(I);
            }
        });
    }

    private ArrayList<Transaction> getTransactionData(final String date){
        final ArrayList<Transaction> transactionArrayList = new ArrayList<>();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<Transaction> transactionRealmResults = realm.where(Transaction.class).findAll();
                if (transactionRealmResults.size() != 0){
                    for (Transaction t: transactionRealmResults){
                        if (t.getDate().equals(date))
                            transactionArrayList.add(t);
                    }
                } else {

                }
            }
        });
        return transactionArrayList;
    }

    private void addCategoryInfo(final String id, final String name, final String categoryType) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Category category = realm.createObject(Category.class, id);
                category.setCategoryName(name);
                category.setCategoryType(categoryType);
            }
        });
    }

    private void setAdapter(){
        Date date2 = Calendar.getInstance().getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = simpleDateFormat.format(date2);
        TransactionAdapter transactionAdapter = new TransactionAdapter(this, getTransactionData(formattedDate));
        recyclerView.setAdapter(transactionAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setAdapter();
    }
}