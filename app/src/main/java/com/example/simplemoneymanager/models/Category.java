package com.example.simplemoneymanager.models;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Category extends RealmObject{

    @PrimaryKey
    private String categoryId;
    private String categoryName;
    private int categoryLmit;

    public Category(){

    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryLmit(int categoryLmit) {
        this.categoryLmit = categoryLmit;
    }

    public int getCategoryLmit() {
        return categoryLmit;
    }
}
