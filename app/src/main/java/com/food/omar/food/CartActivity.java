package com.food.omar.food;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.food.omar.food.Adapter.CartAdapter;
import com.food.omar.food.Adapter.FavoritAdapter;
import com.food.omar.food.Adapter.ProductAdabter;
import com.food.omar.food.DataBase.ModelDB.Cart;
import com.food.omar.food.DataBase.ModelDB.Favorite;
import com.food.omar.food.Model.DataMessage;
import com.food.omar.food.Model.Drink;
import com.food.omar.food.Model.Token;
import com.food.omar.food.Model.myResponce;
import com.food.omar.food.Retrofit.IFCMservice;
import com.food.omar.food.Retrofit.IFoodApi;
import com.food.omar.food.Uitls.Common;
import com.food.omar.food.Uitls.RecycleItemTouchHelper;
import com.food.omar.food.Uitls.RecycleItemTouchHelperListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartActivity extends AppCompatActivity implements RecycleItemTouchHelperListener {
    RecyclerView recyclerProduct;
    Button btn_place_order;
    CompositeDisposable compositeDisposable;
    CartAdapter adabter;
    RelativeLayout rootLayout;
    List<Cart>favoriteList=new ArrayList<>();
    IFoodApi mservise;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        mservise=Common.getAPI();
        compositeDisposable=new CompositeDisposable();
        recyclerProduct=findViewById(R.id.listCart_recycle);
        recyclerProduct.setLayoutManager(new LinearLayoutManager(this));
        recyclerProduct.setHasFixedSize(true);
        rootLayout=findViewById(R.id.rootLayout);
        btn_place_order=findViewById(R.id.btn_place_order);
        btn_place_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlaceOrder();
            }
        });
        ItemTouchHelper.SimpleCallback simpleCallback=new RecycleItemTouchHelper(0,ItemTouchHelper.LEFT,this);
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(recyclerProduct);

        LoadListProduct();
    }

    private void PlaceOrder() {
        if (Common.currentuser !=null) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Submite Order");

            View submiteOrder = LayoutInflater.from(this).inflate(R.layout.submit_order_layout, null);

            final EditText editComment = submiteOrder.findViewById(R.id.edit_comment);
            final EditText editaddress = submiteOrder.findViewById(R.id.edit_address);

            final RadioButton rdiAdress = submiteOrder.findViewById(R.id.rdi_user);
            final RadioButton rdiother = submiteOrder.findViewById(R.id.rdi_other_address);
            rdiAdress.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked)
                        editaddress.setEnabled(false);
                }
            });
            rdiother.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked)
                        editaddress.setEnabled(true);
                }
            });
            builder.setView(submiteOrder);
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).setNegativeButton("Submit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    final String orderComment = editComment.getText().toString();
                    final String orderAdress;
                    if (rdiAdress.isChecked())
                        orderAdress = Common.currentuser.getAddress();
                    else if (rdiother.isChecked())
                        orderAdress = editaddress.getText().toString();
                    else
                        orderAdress = "";
                    compositeDisposable.add(
                            Common.cartRepository.getCartItem()
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribeOn(Schedulers.io())
                                    .subscribe(new Consumer<List<Cart>>() {
                                        @Override
                                        public void accept(List<Cart> carts) throws Exception {
                                            if (!TextUtils.isEmpty(orderAdress))
                                                sendOrderToServer(Common.cartRepository.sumCartItems(),
                                                        carts,
                                                        orderComment, orderAdress);
                                            else
                                                Toast.makeText(CartActivity.this, "Order Address null", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                    );
                }
            });

            builder.show();
        }
        else{

            AlertDialog.Builder builder=new AlertDialog.Builder(CartActivity.this);
            builder.setTitle("Not Login ?");
            builder.setMessage("Please Login Or Register account to submit order");
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    startActivity(new Intent(CartActivity.this,login.class));
                    finish();
                }
            }).show();
        }

    }

    private void sendOrderToServer(float v, List<Cart> carts, String orderComment, String orderAdress) {
if (carts.size() >0)
{
    String orderDitals=new Gson().toJson(carts);
    mservise.submitOrder(orderDitals,Common.currentuser.getPhone(),orderAdress,orderComment,v).enqueue(new Callback<String>() {
        @Override
        public void onResponse(Call<String> call, Response<String> response) {
            sendNotificationToServer(response.body());
            Toast.makeText(CartActivity.this, "order submit", Toast.LENGTH_SHORT).show();
         Common.cartRepository.emptyCart();
         finish();
        }

        @Override
        public void onFailure(Call<String> call, Throwable t) {
            Log.e("ERorr",t.getMessage());
        }
    });
}

    }

    private void sendNotificationToServer(String body) {
mservise.getToken(Common.currentuser.getPhone(),"0").enqueue(new Callback<Token>() {
    @Override
    public void onResponse(Call<Token> call, Response<Token> response) {
        Map<String,String> content_send=new HashMap<>();
content_send.put("title","Omar");
        content_send.put("message","Welcome !");
        DataMessage dataMessage=new DataMessage();
        if (response.body().getToken() !=null )
            dataMessage.setTo(response.body().getToken());
        dataMessage.setData(content_send);

        IFCMservice ifcMservice=Common.getFCMService();
        ifcMservice.sendNotification(dataMessage).enqueue(new Callback<myResponce>() {
            @Override
            public void onResponse(Call<myResponce> call, Response<myResponce> response) {
                if (response.code() == 200)
                {
                    if (response.body().success==1) {
                        Toast.makeText(CartActivity.this, "OrderSubmit !!!", Toast.LENGTH_SHORT).show();
                        Common.cartRepository.emptyCart();
                        finish();
                    }
                    else{
                        Toast.makeText(CartActivity.this, "onFailure", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<myResponce> call, Throwable t) {
                Toast.makeText(CartActivity.this, "onFailure "+t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }

    @Override
    public void onFailure(Call<Token> call, Throwable t) {
        Toast.makeText(CartActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
    }
});
    }


    private void LoadListProduct() {
        compositeDisposable.add(Common.cartRepository.getCartItem()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Cart>>() {
                    @Override
                    public void accept(List<Cart> drinks) throws Exception {
                        displayProductmenu(drinks);
                    }
                }));
    }

    private void displayProductmenu(List<Cart> drinks) {
        favoriteList=drinks;
         adabter=new CartAdapter(this,drinks);
        recyclerProduct.setAdapter(adabter);
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }
    boolean isbackButtonClicked = false;

    @Override
    public void onBackPressed() {

        if (isbackButtonClicked) {
            super.onBackPressed();
            return;
        }
        this.isbackButtonClicked = true;
        Toast.makeText(this, "Please click back again", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        isbackButtonClicked = false;
        super.onResume();
        LoadListProduct();

    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int postion) {

        if (viewHolder instanceof  CartAdapter.CartViewHolder)
        {
            String name =favoriteList.get(viewHolder.getAdapterPosition()).name;
            final Cart delelteItem =favoriteList.get(viewHolder.getAdapterPosition());
            final int deleteIndex =viewHolder.getAdapterPosition();
            adabter.removeItem(deleteIndex);
            Common.cartRepository.deleteCartItem(delelteItem);
            Snackbar snackbar =Snackbar.make(rootLayout,"Remove From Favorite",Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adabter.restoreItem(delelteItem,deleteIndex);
                    Common.cartRepository.insertToCart(delelteItem);
                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        }

    }
}
