package com.food.omar.food;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Toast;

import com.food.omar.food.Adapter.ProductAdabter;
import com.food.omar.food.Model.Drink;
import com.food.omar.food.Retrofit.IFoodApi;
import com.food.omar.food.Uitls.Common;
import com.mancj.materialsearchbar.MaterialSearchBar;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class SearchActivity extends AppCompatActivity {

    List<String>suggest =new ArrayList<>();
    List<Drink>LacalDataSource =new ArrayList<>();
    MaterialSearchBar searchBar;
    IFoodApi mService;
    RecyclerView recyclerView;

    CompositeDisposable compositeDisposable=new CompositeDisposable();
    ProductAdabter searchAdapter,adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mService= Common.getAPI();

        recyclerView=findViewById(R.id.recycle_Search);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));

        searchBar=findViewById(R.id.searchbar);
        searchBar.setHint("Enter Your Product");

        LoadListProduct();

        searchBar.setCardViewElevation(10);
        searchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                List<String>suggestsss=new ArrayList<>();
                for (String search :suggest)
                {
                    if (search.toLowerCase().contains(searchBar.getText().toLowerCase()))
                        suggestsss.add(search);
                }

                searchBar.setLastSuggestions(suggestsss);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        searchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                if (!enabled)
                    recyclerView.setAdapter(adapter);
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                startSearch(text);
            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });
    }

    private void startSearch(CharSequence text) {
        List<Drink>result =new ArrayList<>();
        for (Drink drink:LacalDataSource)
           if (drink.Name.contains(text))
               result.add(drink);
        searchAdapter=new ProductAdabter(this,result);
        recyclerView.setAdapter(searchAdapter);
    }

    private void LoadListProduct() {
        compositeDisposable.add(mService.getProducts()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Drink>>() {
                    @Override
                    public void accept(List<Drink> drinks) throws Exception {
                        displayProductmenu(drinks);
                        suggestProductmenu(drinks);

                    }
                }));
    }

    private void suggestProductmenu(List<Drink> drinks) {
        for (Drink drink :drinks)
            suggest.add(drink.Name);
        searchBar.setLastSuggestions(suggest);
    }

    private void displayProductmenu(List<Drink> drinks) {
        LacalDataSource=drinks;
        adapter=new ProductAdabter(this,drinks);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }
}
