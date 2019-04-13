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
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.simplemoneymanager.models.Category;
import com.google.firebase.auth.FirebaseAuth;
import com.jaeger.library.StatusBarUtil;

import java.util.Random;

import io.realm.Realm;

public class UserActivity extends AppCompatActivity {
    Button btnLogOut;
    FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FloatingActionButton floatingActionButton;

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        btnLogOut = (Button) findViewById(R.id.btnLogOut);
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

    private void addCategoryInfo(String id, String name, String categoryType){
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        Category category = realm.createObject(Category.class);
        category.setCategoryId(id);
        category.setCategoryName(name);
        category.setCategoryType(categoryType);
        realm.commitTransaction();
    }
}