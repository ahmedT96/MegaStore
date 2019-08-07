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

public class FavoritAdapter extends RecyclerView.Adapter<FavoritAdapter.FavoritViewHolder> {

    Context context;
    List<Favorite>cartList;

    public FavoritAdapter(Context context, List<Favorite> cartList) {
        this.context = context;
        this.cartList = cartList;
    }

    @NonNull
    @Override
    public FavoritViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemview= LayoutInflater.from(context).inflate(R.layout.favorit_item,null);
        return new FavoritViewHolder(itemview);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoritViewHolder holder, final int position) {
        Picasso.with(context)
                .load(cartList.get(position).Link)
                .into(holder.imageProduct);



        holder.txt_extera.setText(String.valueOf(cartList.get(position).price));
        holder.txt_product.setText(cartList.get(position).name);


    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

   public class FavoritViewHolder extends RecyclerView.ViewHolder {
        ImageView imageProduct;
        public RelativeLayout id_background;
         public  LinearLayout id_foreground;
        TextView txt_product,txt_extera,txt_price;
        ElegantNumberButton txt_amount;
        public FavoritViewHolder(View itemView)
        { super(itemView);

            imageProduct=itemView.findViewById(R.id.image_fav);
            id_background=itemView.findViewById(R.id.id_background);

            id_foreground=itemView.findViewById(R.id.id_foreground);

            txt_product=itemView.findViewById(R.id.txt_fav_name);
            txt_extera=itemView.findViewById(R.id.txt_Fav_extera);

        }


    }
    public void removeItem(int position)
    {
        cartList.remove(position);
        notifyItemRemoved(position);
    }
    public void restoreItem(Favorite item,int position)
    {
        cartList.add(position,item);
        notifyItemInserted(position);
    }

}
