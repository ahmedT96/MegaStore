package com.food.omar.food.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.food.omar.food.DataBase.ModelDB.Cart;
import com.food.omar.food.DataBase.ModelDB.Favorite;
import com.food.omar.food.R;
import com.food.omar.food.Uitls.Common;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    Context context;
    List<Cart>cartList;

    public CartAdapter(Context context, List<Cart> cartList) {
        this.context = context;
        this.cartList = cartList;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemview= LayoutInflater.from(context).inflate(R.layout.cart_item,null);
        return new CartViewHolder(itemview);
    }

    @Override
    public void onBindViewHolder(@NonNull final CartViewHolder holder, final int position) {
        Picasso.with(context)
                .load(cartList.get(position).image)
                .into(holder.imageProduct);
        final double priceOfCup=cartList.get(position).price/cartList.get(position).amount;

        holder.txt_amount.setNumber(String.valueOf(cartList.get(position).amount));
        holder.txt_price.setText(cartList.get(position).price+" $");

        holder.txt_extera.setText(cartList.get(position).toppingExtera);
        holder.txt_product.setText(cartList.get(position).name);
        holder.txt_amount.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                Cart cart=cartList.get(position);
                cart.amount=newValue;
                cart.price=Math.round(priceOfCup*newValue);
                Common.cartRepository.updateToCart(cart);
                holder.txt_price.setText(cartList.get(position).price+" $");

            }
        });

    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public class CartViewHolder extends RecyclerView.ViewHolder {
       ImageView imageProduct;
       TextView txt_product,txt_extera,txt_price;
       ElegantNumberButton txt_amount;
        public RelativeLayout view_background;
        public LinearLayout view_foreground;
       public CartViewHolder(View itemView)
       { super(itemView);

           imageProduct=itemView.findViewById(R.id.image_cart);
           txt_product=itemView.findViewById(R.id.txt_cart_name);
           txt_extera=itemView.findViewById(R.id.txt_extera);
           txt_price=itemView.findViewById(R.id.txt_price);
           txt_amount=itemView.findViewById(R.id.id_amount);

           view_background=itemView.findViewById(R.id.view_background);

           view_foreground=itemView.findViewById(R.id.view_foreground);

       }


   }

    public void removeItem(int position)
    {
        cartList.remove(position);
        notifyItemRemoved(position);
    }
    public void restoreItem(Cart item, int position)
    {
        cartList.add(position,item);
        notifyItemInserted(position);
    }
}
