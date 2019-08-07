package com.food.omar.food.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.food.omar.food.Home;
import com.food.omar.food.Interface.ItemClickListener;
import com.food.omar.food.Model.Category;
import com.food.omar.food.ProductActivity;
import com.food.omar.food.R;
import com.food.omar.food.Uitls.Common;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CategoryAdabter extends RecyclerView.Adapter<CategoryViewHolder>{
  Context context;
  List<Category> categories;

    public CategoryAdabter(Context context, List<Category> categories) {
        this.context = context;
        this.categories = categories;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemview= LayoutInflater.from(context).inflate(R.layout.layout_inflator_item,null);
        return new CategoryViewHolder(itemview);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, final int position) {
        Picasso.with(context)
                .load(categories.get(position).Link)
                .into(holder.image);

        holder.text.setText(categories.get(position).Name);
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view) {
                Common.currentCategory=categories.get(position);
                context.startActivity(new Intent(context, ProductActivity.class));
            }
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }
}
