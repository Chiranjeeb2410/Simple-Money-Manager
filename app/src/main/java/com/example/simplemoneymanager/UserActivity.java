package com.example.simplemoneymanager;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.simplemoneymanager.adapters.TransactionAdapter;
import com.example.simplemoneymanager.helpers.PreferencesUtil;
import com.example.simplemoneymanager.models.Category;
import com.example.simplemoneymanager.models.Transaction;
import com.example.simplemoneymanager.models.UserPrefs;
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

    private TextView textView;

    private PreferencesUtil SP;

    private TextView monthly_limit_Textview;
    private TextView balanceTextView;
    private TextView expensesTextView;

    private final int[] totalAmt = {0};

    private final int[] amount = {0};

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;
    private Toolbar toolbar;

    private View.OnClickListener onClickListener= new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Transaction transaction = (Transaction)view.findViewById(R.id.amount_text).getTag();
            Intent intent = new Intent(UserActivity.this, TransactionActivity.class);
            intent.putExtra("edit_trans", true);
            intent.putExtra("category", transaction.getCategory());
            intent.putExtra("amount", transaction.getAmount());
            intent.putExtra("memo", transaction.getMemo());
            intent.putExtra("date", transaction.getDate());
            intent.putExtra("id", transaction.getTransactionId());
            Log.i("gerty", transaction.getTransactionId());
            startActivity(intent);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        Realm.init(getApplicationContext());
        realm = Realm.getDefaultInstance();
        // btnLogOut = (Button) findViewById(R.id.btnLogOut);
        recyclerView = findViewById(R.id.expense_item_view);
        SP = PreferencesUtil.getInstance(this);
        textView = findViewById(R.id.gomn);
        monthly_limit_Textview = findViewById(R.id.montly_limit);
        balanceTextView = findViewById(R.id.balance);
        expensesTextView = findViewById(R.id.todays_expenses);
        Date date2 = Calendar.getInstance().getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = simpleDateFormat.format(date2);
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

                            case 2:
                                AlertDialog.Builder monthlyLimitDialog = new AlertDialog.Builder(UserActivity.this);
                                View view2 = getLayoutInflater().inflate(R.layout.dialog_add_limit, null);
                                final EditText monthlyLimit = view2.findViewById(R.id.monthly_limit);
                                monthlyLimitDialog.setView(view2);
                                monthlyLimitDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        if (monthlyLimit.getText().toString().isEmpty()){
                                            Toast.makeText(getApplicationContext(), "Enter valid limit", Toast.LENGTH_SHORT).show();
                                        } else {

                                        }
                                        monthly_limit_Textview.setText(SP.getString("monthly_limit", "0"));
                                    }
                                });
                                monthlyLimitDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                });
                                AlertDialog dialog2 = monthlyLimitDialog.create();
                                dialog2.show();
                        }
                    }
                });
                Dialog dialog = builder.create();
                dialog.show();
            }
        });

        // logout user through firebaseAut
    }

    private ArrayList<Transaction> getTransactionData(){
        final ArrayList<Transaction> transactionArrayList = new ArrayList<>();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<Transaction> transactionRealmResults = realm.where(Transaction.class).findAll();
                if (transactionRealmResults.size() != 0){
                    textView.setVisibility(View.GONE);
                    for (Transaction t: transactionRealmResults){
                        amount[0] = amount[0] + Integer.parseInt(t.getAmount());
                        transactionArrayList.add(t);
                    }
                } else {
                    textView.setVisibility(View.VISIBLE);
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
        balanceTextView.setText(String.valueOf(Integer.parseInt(SP.getString("monthly_limit", "0")) - amount[0]));
        expensesTextView.setText(String.valueOf(amount[0]));
        Date date2 = Calendar.getInstance().getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = simpleDateFormat.format(date2);
        TransactionAdapter transactionAdapter = new TransactionAdapter(this, getTransactionData());
        transactionAdapter.setOnClickListener(onClickListener);
        recyclerView.setAdapter(transactionAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setAdapter();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.action_logout:
                signout();
                return true;

            case R.id.user_info:
                AlertDialog.Builder monthlyLimitDialog = new AlertDialog.Builder(UserActivity.this);
                View view2 = getLayoutInflater().inflate(R.layout.user_info, null);
                String name = SP.getString("username", "none");
                String email = SP.getString("emailID", "none");
                String monthlylimit = SP.getString("monthly_limit", "0");
                TextView Uname = view2.findViewById(R.id.username);
                Uname.setText(name);
                TextView userEmail = view2.findViewById(R.id.email);
                userEmail.setText(email);
                TextView mothlylimit = view2.findViewById(R.id.monthly_limit_value);
                mothlylimit.setText(monthlylimit);
                monthlyLimitDialog.setView(view2);
                monthlyLimitDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog dialog = monthlyLimitDialog.create();
                dialog.show();
                return true;

            case R.id.view_cat:
                Intent in1 = new Intent(UserActivity.this, CategoryActivity.class);
                startActivity(in1);
                finish();
                return true;

            case R.id.set_menu:
                Intent in2 = new Intent(UserActivity.this, SettingsActivity.class);
                startActivity(in2);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void signout() {
        FirebaseAuth.getInstance().signOut();
        Intent in = new Intent(UserActivity.this, LoginActivity.class);
        in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(in);
        finish();
    }
}




