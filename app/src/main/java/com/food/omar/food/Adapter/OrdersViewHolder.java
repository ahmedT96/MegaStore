package com.food.omar.food.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.food.omar.food.Interface.ItemClickListener;
import com.food.omar.food.R;

public class OrdersViewHolder extends RecyclerView.ViewHolder  {


    public TextView textID,textAddress,textStatues,textComment,textprice;


    public OrdersViewHolder(View itemview) {
        super(itemview);
        textComment=itemView.findViewById(R.id.Order_item_name_div);
        textprice=itemView.findViewById(R.id.Order_item_price_div);
        textID=itemView.findViewById(R.id.Order_item_ID_div);
        textAddress=itemView.findViewById(R.id.Order_item_address_div);


    }


}
