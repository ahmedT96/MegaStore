package com.food.omar.food.Adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.food.omar.food.Home;
import com.food.omar.food.Interface.ItemClickListener;
import com.food.omar.food.Model.Category;
import com.food.omar.food.Model.Drink;
import com.food.omar.food.ProductActivity;
import com.food.omar.food.R;
import com.food.omar.food.Uitls.Common;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductAdabter extends RecyclerView.Adapter<ProductViewHolder>{
    Context context;
    List<Drink> categories;

    public ProductAdabter(Context context, List<Drink> categories) {
        this.context = context;
        this.categories = categories;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemview= LayoutInflater.from(context).inflate(R.layout.layout_inflator_item_product,null);
        return new ProductViewHolder(itemview);
    }

    @Override
    public void onBindViewHolder(@NonNull final ProductViewHolder holder, final int position) {
        Picasso.with(context)
                .load(categories.get(position).Link)
                .into(holder.image);
        holder.btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialg(position);
            }
        });
        holder.textname.setText(categories.get(position).Name);
        holder.textprice.setText(categories.get(position).Price+" $");
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view) {
                //Common.currentCategory=categories.get(position);
               // context.startActivity(new Intent(context, ProductActivity.class));
                Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        if (Common.favoritRespository.isFavoritDAO(Integer.parseInt(categories.get(position).ID))==1)
            holder.btnFavort.setImageResource(R.drawable.ic_favorite_black_24dp);
        else
            holder.btnFavort.setImageResource(R.drawable.ic_favorite_border_black_24dp);

        holder.btnFavort.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (Common.favoritRespository.isFavoritDAO(Integer.parseInt(categories.get(position).ID))!=1) {

                    addOrRemoveFavotit(categories.get(position),true);
                    holder.btnFavort.setImageResource(R.drawable.ic_favorite_black_24dp);
                }
                else {

                    addOrRemoveFavotit(categories.get(position),false);
                    holder.btnFavort.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                }
            }
        });
    }

    private void addOrRemoveFavotit(Drink drink, boolean IsAdd) {
        Favorite favorite=new Favorite();
        favorite.id= Integer.parseInt(drink.ID);
        favorite.Link=drink.Link;
        favorite.name=drink.Name;
        favorite.price= Double.parseDouble(drink.Price);
        favorite.menuID=drink.MenuId;
        if (IsAdd)
            Common.favoritRespository.insertToFav(favorite);
        else
            Common.favoritRespository.deleteFavItem(favorite);

    }


    private void showDialg(final int position) {
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        View itemview=LayoutInflater.from(context).inflate(R.layout.cart_item_layout,null);

        ImageView img_product=(ImageView) itemview.findViewById(R.id.img_cart_product);
        final ElegantNumberButton elegantNumberButton=itemview.findViewById(R.id.txt_count);
        TextView txt_Product=itemview.findViewById(R.id.txt_cart_product_name);
        final EditText comment=itemview.findViewById(R.id.edt_comment);
        Button Confirm= itemview.findViewById(R.id.ok_confirm);

        RadioButton sizem=itemview.findViewById(R.id.SizeM);
        RadioButton sizel=itemview.findViewById(R.id.SizeL);
        RadioButton sizexl=itemview.findViewById(R.id.SizeXL);

        sizem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked)
            {
                Common.sizes=1;
            }
    }
});
        sizel.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    Common.sizes=2;
                }
            }
        });
        sizexl.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    Common.sizes=3;
                }
            }
        });

        RadioButton white=itemview.findViewById(R.id.Whit);
        RadioButton red=itemview.findViewById(R.id.red);
        RadioButton blue=itemview.findViewById(R.id.blue);
        RadioButton black=itemview.findViewById(R.id.black);
        RadioButton orange=itemview.findViewById(R.id.orange);
        white.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    Common.colors=1;
                }
            }
        });
        red.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    Common.colors=2;
                }
            }
        });
        blue.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    Common.colors=3;
                }
            }
        });
        black.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    Common.colors=4;
                }
            }
        });
        orange.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    Common.colors=5;
                }
            }
        });
       Picasso.with(context).load(categories.get(position).Link).into(img_product);
        txt_Product.setText(categories.get(position).Name);
        builder.setView(itemview);
        builder.show();





        Confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Common.sizes==-1 || Common.colors==-1)
                {
                    Toast.makeText(context, "Plez Choose your Size and Color", Toast.LENGTH_SHORT).show();
                }
                else
                {

                    if (Integer.parseInt(elegantNumberButton.getNumber()) == 0) {
                        Toast.makeText(context, "Plez choose your number", Toast.LENGTH_SHORT).show();

                    } else {
                        Common.currenfinalComment = comment.getText().toString();
                        showDialgConfirm(position, elegantNumberButton.getNumber());
                    }
                }
            }
        });

    }

    @SuppressLint("ResourceAsColor")
    private void showDialgConfirm(final int position, final String number) {
        int sizes=Common.sizes;
        int colors=Common.colors;

        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        View itemview=LayoutInflater.from(context).inflate(R.layout.layout_conifrm,null);

        ImageView img_product=(ImageView) itemview.findViewById(R.id.img_Product);
        final TextView txt_Product=itemview.findViewById(R.id.txt_product_name);
        final TextView txt_Product_price=itemview.findViewById(R.id.txtproduct_price);
        final TextView extera=itemview.findViewById(R.id.commentExtera);

        TextView sizee=itemview.findViewById(R.id.sizee);
        TextView colo=itemview.findViewById(R.id.txt_color);

        Picasso.with(context).load(categories.get(position).Link).into(img_product);
        txt_Product.setText(categories.get(position).Name);
        extera.setText(Common.currenfinalComment);
        final double Price=Double.parseDouble(categories.get(position).Price)*Double.parseDouble(number);
        txt_Product_price.setText(String.valueOf(Double.parseDouble(categories.get(position).Price)*Double.parseDouble(number))+" $");
        if (sizes==1){sizee.setText("Size M");}else  if (sizes==2){sizee.setText("Size L");}else  if (sizes==3){sizee.setText("Size XL");};
        if (colors==1){colo.setText("White");}
        else  if (colors==2){colo.setText("Red");colo.setBackgroundResource(R.color.red);}
        else  if (colors==3){colo.setText("Blue");colo.setBackgroundResource(R.color.blue);}
            else  if (colors==4){colo.setText("Black");colo.setBackgroundResource(R.color.black);}
            else  if (colors==5){colo.setText("Orange");colo.setBackgroundResource(R.color.orange);};

        builder.setView(itemview);

        builder.setNegativeButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                try {

                dialog.dismiss();
                Cart cartitem=new Cart();
                cartitem.name=txt_Product.getText().toString();
                cartitem.amount=Integer.parseInt(number);
                cartitem.ice=Common.sizes;
                cartitem.suger=Common.colors;
                cartitem.price=Price;
                cartitem.toppingExtera=extera.getText().toString();
                cartitem.image=categories.get(position).Link;
                Common.cartRepository.insertToCart(cartitem);

                Log.d("Omar",new Gson().toJson(cartitem));
                Toast.makeText(context, "Save item to Cart seccess", Toast.LENGTH_SHORT).show();
                }
                catch (Exception ex)
                {
                    Toast.makeText(context,ex.getMessage() , Toast.LENGTH_SHORT).show();
                }

            }
        });
        builder.show();
    }


    @Override
    public int getItemCount() {
        return categories.size();
    }
}
