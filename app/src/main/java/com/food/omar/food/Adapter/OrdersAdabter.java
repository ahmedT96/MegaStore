package com.food.omar.food.Adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.food.omar.food.DataBase.ModelDB.Cart;
import com.food.omar.food.DataBase.ModelDB.Favorite;
import com.food.omar.food.Interface.ItemClickListener;
import com.food.omar.food.Model.Drink;
import com.food.omar.food.Model.orders;
import com.food.omar.food.R;
import com.food.omar.food.Uitls.Common;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.List;

public class OrdersAdabter extends RecyclerView.Adapter<OrdersViewHolder>{
    Context context;
    List<orders> Orders;

    public OrdersAdabter(Context context,  List<orders> Orders) {
        this.context = context;
        this.Orders = Orders;
    }

    @NonNull
    @Override
    public OrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemview= LayoutInflater.from(context).inflate(R.layout.layout_order_inflate,parent,false);
        return new OrdersViewHolder(itemview);
    }

    @Override
    public void onBindViewHolder(@NonNull final OrdersViewHolder holder, final int position) {


        holder.textID.setText("#"+(int) Orders.get(position).getOrderId());
        holder.textprice.setText(Orders.get(position).getOrderPrice()+" $");
        holder.textAddress.setText(Orders.get(position).getOrderAddress());
        holder.textComment.setText(Orders.get(position).getOrderAddress());
        holder.textStatues.setText("Order : "+Common.convertCodeToStatue(Orders.get(position).getOrderStatus()));
    }

    @Override
    public int getItemCount() {
        return Orders.size();
    }
}
