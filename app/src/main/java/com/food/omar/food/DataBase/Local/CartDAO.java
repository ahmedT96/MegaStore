package com.food.omar.food.DataBase.Local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.food.omar.food.DataBase.ModelDB.Cart;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface CartDAO {

    @Query("SELECT * FROM Cart")
    Flowable<List<Cart>> getCartItem();

    @Query("SELECT * FROM Cart WHERE id=:cartItemID")
    Flowable<List<Cart>> getCartItemByID(int cartItemID);

    @Query("DELETE  FROM Cart")
    void emptyCart();

    @Query("SELECT COUNT(*) from Cart")
    int countCartItems();

    @Query("SELECT SUM(price) from Cart")
    int sumCartItems();
    @Insert
    void insertToCart(Cart...carts);

    @Update
    void updateToCart(Cart...carts);

    @Delete
    void deleteCartItem(Cart cart);


}
