package com.food.omar.food;

import android.arch.persistence.room.Entity;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.food.omar.food.Adapter.CartAdapter;
import com.food.omar.food.Adapter.FavoritAdapter;
import com.food.omar.food.DataBase.ModelDB.Cart;
import com.food.omar.food.DataBase.ModelDB.Favorite;
import com.food.omar.food.Uitls.Common;
import com.food.omar.food.Uitls.RecycleItemTouchHelper;
import com.food.omar.food.Uitls.RecycleItemTouchHelperListener;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;


public class FavoriteActivity extends AppCompatActivity implements RecycleItemTouchHelperListener{
    RecyclerView recyclerProduct;
    CompositeDisposable compositeDisposable;
    FavoritAdapter adabter;
    RelativeLayout rootLayout;
    List<Favorite>favoriteList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        compositeDisposable=new CompositeDisposable();
        recyclerProduct=findViewById(R.id.listfav_recycle);
        rootLayout=findViewById(R.id.rootLayout);
        recyclerProduct.setLayoutManager(new LinearLayoutManager(this));
        recyclerProduct.setHasFixedSize(true);
        ItemTouchHelper.SimpleCallback simpleCallback=new RecycleItemTouchHelper(0,ItemTouchHelper.LEFT,this);
       new ItemTouchHelper(simpleCallback).attachToRecyclerView(recyclerProduct);
        LoadListProduct();
    }

    private void LoadListProduct() {
        compositeDisposable.add(Common.favoritRespository.getFavtItem()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Favorite>>() {
                    @Override
                    public void accept(List<Favorite> drinks) throws Exception {
                        displayProductmenu(drinks);
                    }
                }));
    }

    private void displayProductmenu(List<Favorite> drinks) {
         adabter=new FavoritAdapter(this,drinks);
        favoriteList=drinks;
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
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int postion) {
if (viewHolder instanceof FavoritAdapter.FavoritViewHolder)
{
    String name =favoriteList.get(viewHolder.getAdapterPosition()).name;
    final Favorite delelteItem =favoriteList.get(viewHolder.getAdapterPosition());
    final int deleteIndex =viewHolder.getAdapterPosition();
    adabter.removeItem(deleteIndex);
    Common.favoritRespository.deleteFavItem(delelteItem);
    Snackbar snackbar =Snackbar.make(rootLayout,"Remove From Favorite",Snackbar.LENGTH_LONG);
    snackbar.setAction("UNDO", new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            adabter.restoreItem(delelteItem,deleteIndex);
            Common.favoritRespository.insertToFav(delelteItem);
        }
    });
    snackbar.setActionTextColor(Color.YELLOW);
    snackbar.show();
}
    }
}
