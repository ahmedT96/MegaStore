package com.food.omar.food.Adapter;

import android.content.ClipData;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.food.omar.food.Interface.ItemClickListener;
import com.food.omar.food.Model.Category;
import com.food.omar.food.R;
import com.food.omar.food.Uitls.Common;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductViewHolder  extends RecyclerView.ViewHolder implements View.OnClickListener {


    public TextView textname,textprice;
    public ImageView image;
    public ImageView btnCart,btnFavort;

    ItemClickListener itemClickListener;
    public  void setItemClickListener(ItemClickListener itemClickListener)
    {
        this.itemClickListener=itemClickListener;
    }
    public ProductViewHolder(View itemview) {
        super(itemview);
        textname=itemView.findViewById(R.id.txt_Product);
        textprice=itemView.findViewById(R.id.txt_price);
        image=itemView.findViewById(R.id.image_prod);
        btnCart=itemView.findViewById(R.id.addtocart);
        btnFavort=itemView.findViewById(R.id.addtoFav);

        itemview.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v);
    }
}
