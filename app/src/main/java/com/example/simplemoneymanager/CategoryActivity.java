package com.example.simplemoneymanager;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;

import com.example.simplemoneymanager.adapters.CategoryAdapter;
import com.example.simplemoneymanager.models.Category;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

public class CategoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    Realm realm;

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            final Category category = (Category) view.findViewById(R.id.category_name).getTag();
            AlertDialog.Builder builder1 = new AlertDialog.Builder(CategoryActivity.this);
            View view1 = getLayoutInflater().inflate(R.layout.dialog_add_category, null);
            builder1.setView(view1);
            final EditText categoryName = view1.findViewById(R.id.category_name);
            categoryName.setText(category.getCategoryName());
            final EditText categoryLimit = view1.findViewById(R.id.category_limit);
            categoryLimit.setText(category.getCategoryType());
            builder1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            Category category1 = realm.where(Category.class).equalTo("categoryId",
                                    category.getCategoryId()).findFirst();
                            category1.setCategoryName(category.getCategoryName());
                            category1.setCategoryType(categoryLimit.getText().toString());
                            realm.copyToRealmOrUpdate(category1);
                        }
                    });
                    setAdapter();
                    dialogInterface.dismiss();
                }
            });
            builder1.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            AlertDialog alertDialog = builder1.create();
            alertDialog.show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        getSupportActionBar().setTitle("Categories");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        realm = Realm.getDefaultInstance();
        recyclerView = findViewById(R.id.category_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
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
        for (final Category category: categories){
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    Category category1 = realm.createObject(Category.class);
                    category1.setCategoryId(category.getCategoryId());
                    category1.setCategoryName(category.getCategoryName());
                    category1.setCategoryType(category.getCategoryType());
                }
            });
        }
    }

    private ArrayList<Category> getCategoryData(){
        final ArrayList<Category> categories = new ArrayList<>();
        Realm realm = Realm.getDefaultInstance();
        if (realm.where(Category.class).findAll().size() == 0)
            addCategoriesForFirstTimeLaunch();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<Category> realmResults = realm.where(Category.class).findAll();
                for (Category category: realmResults){
                    categories.add(category);
                }
            }
        });
        return categories;
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    private void setAdapter(){
        CategoryAdapter categoryAdapter = new CategoryAdapter(this, getCategoryData());
        categoryAdapter.setOnClickListener(onClickListener);
        recyclerView.setAdapter(categoryAdapter);
    }

    // retrieves only category name for spinner
    public ArrayList<String> getCategoryName(){
        Realm realm = Realm.getDefaultInstance();
        ArrayList<String> categories = new ArrayList<>();
        RealmResults<Category> realmResults = realm.where(Category.class).findAll();
        for (Category category: realmResults){
            categories.add(category.getCategoryName());
        }
        return categories;
    }

}
