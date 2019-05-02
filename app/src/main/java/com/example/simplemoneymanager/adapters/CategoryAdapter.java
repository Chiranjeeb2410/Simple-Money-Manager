package com.example.simplemoneymanager.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.simplemoneymanager.R;


import com.example.simplemoneymanager.models.Category;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder>{

    private ArrayList<Category> categoryArrayList;
    private Context context;

    public CategoryAdapter(Context context, ArrayList<Category> categoryArrayList) {
       this.context = context;
       this.categoryArrayList = categoryArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.category_item_view, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        Category category = categoryArrayList.get(i);
        viewHolder.expenseCategory.setText(category.getCategoryName());
        viewHolder.expenseType.setText("\u20B9"+" "+category.getCategoryType());
        viewHolder.expenseCategory.setTag(category);

    }

    @Override
    public int getItemCount() {
        return categoryArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private TextView expenseCategory;
        private TextView expenseType;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            expenseCategory = itemView.findViewById(R.id.category_name);
            expenseType = itemView.findViewById(R.id.expense_cat);
        }
    }
}
