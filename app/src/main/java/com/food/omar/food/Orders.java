package com.food.omar.food;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import com.food.omar.food.Adapter.OrdersAdabter;
import com.food.omar.food.Adapter.ProductAdabter;
import com.food.omar.food.Model.Drink;
import com.food.omar.food.Model.orders;
import com.food.omar.food.Retrofit.IFoodApi;
import com.food.omar.food.Uitls.Common;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class Orders extends AppCompatActivity {
    RecyclerView recyclerProduct;
    IFoodApi mServise;
    CompositeDisposable compositeDisposable=new CompositeDisposable();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
        mServise= Common.getAPI();
      recyclerProduct=findViewById(R.id.orders_view);
        recyclerProduct.setLayoutManager(new LinearLayoutManager(this));
        recyclerProduct.setHasFixedSize(true);
      LoadListProduct();
    }
    private void LoadListProduct() {
        if (Common.currentuser!=null) {
            Toast.makeText(Orders.this, "gojo", Toast.LENGTH_SHORT).show();

            compositeDisposable.add(mServise.getOrders(Common.currentuser.getPhone(), "0")
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Consumer<List<orders>>() {
                        @Override
                        public void accept(List<orders> order) throws Exception {
                            displayProductmenu(order);
                            Toast.makeText(Orders.this, String.valueOf(order) , Toast.LENGTH_SHORT).show();
                        }

                    }));
        }
        else
            Toast.makeText(this, "Please login !", Toast.LENGTH_SHORT).show();
        finish();
    }
    private void displayProductmenu(List<orders> order) {
        OrdersAdabter adabter=new OrdersAdabter(this,order);
        recyclerProduct.setAdapter(adabter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        LoadListProduct();

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
}
