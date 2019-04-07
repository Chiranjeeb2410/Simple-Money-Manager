package com.example.simplemoneymanager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.simplemoneymanager.adapters.CategoryAdapter;
import com.example.simplemoneymanager.models.Category;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

public class CategoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        getSupportActionBar().setTitle("Categories");
        recyclerView = findViewById(R.id.category_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        CategoryAdapter categoryAdapter = new CategoryAdapter(this, getCategoryData());
        recyclerView.setAdapter(categoryAdapter);
    }

    private ArrayList<Category> getCategoryData(){
        final ArrayList<Category> categories = new ArrayList<>();
        Realm realm = Realm.getDefaultInstance();
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
}
