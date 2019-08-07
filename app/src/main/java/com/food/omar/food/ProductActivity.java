package com.food.omar.food;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import com.food.omar.food.Adapter.CategoryAdabter;
import com.food.omar.food.Adapter.ProductAdabter;
import com.food.omar.food.Model.Category;
import com.food.omar.food.Model.Drink;
import com.food.omar.food.Model.User;
import com.food.omar.food.Retrofit.IFoodApi;
import com.food.omar.food.Uitls.Common;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ProductActivity extends AppCompatActivity {
    RecyclerView recyclerProduct;
    IFoodApi mServise;
    TextView tital;
    CompositeDisposable compositeDisposable=new CompositeDisposable();
SwipeRefreshLayout swipeRefreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        getSupportActionBar().hide();
        mServise=Common.getAPI();
        tital=findViewById(R.id.txt_menu_item);
        swipeRefreshLayout=findViewById(R.id.swip_to_refresh);
        tital.setText(Common.currentCategory.Name);
        recyclerProduct=findViewById(R.id.recycle_product);
        recyclerProduct.setLayoutManager(new GridLayoutManager(this,2));
        recyclerProduct.setHasFixedSize(true);
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                LoadListProduct(Common.currentCategory.ID);

            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                LoadListProduct(Common.currentCategory.ID);
            }
        });
    }

    private void LoadListProduct(String id) {
        compositeDisposable.add(mServise.getdrink(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Drink>>() {
                    @Override
                    public void accept(List<Drink> drinks) throws Exception {
                        displayProductmenu(drinks);
                    }
                }));

    }
    private void displayProductmenu(List<Drink> drinks) {
        ProductAdabter adabter=new ProductAdabter(this,drinks);
        recyclerProduct.setAdapter(adabter);
        swipeRefreshLayout.setRefreshing(false);
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
    protected void onPostResume() {
        super.onPostResume();
        isbackButtonClicked = false;
    }
}
