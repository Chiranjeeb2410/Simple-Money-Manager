package com.example.simplemoneymanager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.print.PrinterId;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.simplemoneymanager.models.Category;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.jaeger.library.StatusBarUtil;

import java.util.ArrayList;

import io.realm.Realm;

public class MainActivity extends AppCompatActivity {
    public EditText emailId, passwd;
    Button btnSignUp, newPassButton;
    TextView signIn;
    FirebaseAuth firebaseAuth;

    private SharedPreferences SP;

    private ProgressBar signUpProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        SP = PreferenceManager.getDefaultSharedPreferences(this);
        /**String firstLaunch = SP.getString("firstLaunch", "no");
        if (firstLaunch.equals("no")){
            addCategoriesForFirstTimeLaunch();
            SharedPreferences.Editor editor = SP.edit();
            editor.putString("firstLaunch", "yes");
        }*/
        StatusBarUtil.setTransparent(this);
        firebaseAuth = FirebaseAuth.getInstance();
        emailId = findViewById(R.id.ETemail);
        passwd = findViewById(R.id.ETpassword);
        btnSignUp = findViewById(R.id.btnSignUp);
        signUpProgress = findViewById(R.id.signup_progressbar);
        signIn = findViewById(R.id.TVSignIn);
      //  newPassButton = findViewById(R.id.forgotPass);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUpProgress.setVisibility(View.VISIBLE);
                String emailID = emailId.getText().toString();
                String paswd = passwd.getText().toString();
                if (emailID.isEmpty()) {
                    emailId.setError("Provide your Email first!");
                    emailId.requestFocus();
                } else if (paswd.isEmpty()) {
                    passwd.setError("Set your password");
                    passwd.requestFocus();
                } else if (emailID.isEmpty() && paswd.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Fields Empty!", Toast.LENGTH_SHORT).show();
                } else if (!(emailID.isEmpty() && paswd.isEmpty())) {
                    firebaseAuth.createUserWithEmailAndPassword(emailID, paswd).addOnCompleteListener(MainActivity.this, new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            signUpProgress.setVisibility(View.GONE);
                            if (!task.isSuccessful()) {
                                Toast.makeText(MainActivity.this.getApplicationContext(),
                                        "SignUp unsuccessful: " + task.getException().getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                startActivity(new Intent(MainActivity.this, UserActivity.class));
                            }
                        }
                    });
                } else {
                    Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent I = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(I);
            }
        });
    }

    private void addCategoriesForFirstTimeLaunch(){
        Realm realm = Realm.getDefaultInstance();
        ArrayList<Category> categories = new ArrayList<>();
        Category c1 = new Category();
        c1.setCategoryName("Food");
        c1.setCategoryType("Expense");
        c1.setCategoryId("FOO13");
        categories.add(c1);
        Category c2 = new Category();
        c2.setCategoryName("Transportation");
        c2.setCategoryType("Expense");
        c2.setCategoryId("TRA23");
        categories.add(c2);
        Category c3 = new Category();
        c3.setCategoryName("Electricity");
        c3.setCategoryType("Expense");
        c3.setCategoryId("ELE45");
        categories.add(c3);
        Category c4 = new Category();
        c4.setCategoryName("Entertainment");
        c4.setCategoryType("Expense");
        c4.setCategoryId("ENT56");
        categories.add(c4);
        Category c5 = new Category();
        c5.setCategoryName("Bills");
        c5.setCategoryType("Expense");
        c5.setCategoryId("BIL65");
        categories.add(c5);
        Category c6 = new Category();
        c6.setCategoryName("Salary");
        c6.setCategoryType("Income");
        c6.setCategoryId("SAL25");
        categories.add(c6);
        for (Category category: categories){
            realm.beginTransaction();
            Category category1 = realm.createObject(Category.class);
            category1.setCategoryId(category.getCategoryId());
            category1.setCategoryName(category.getCategoryName());
            category1.setCategoryType(category.getCategoryType());
            realm.commitTransaction();
        }
    }
}