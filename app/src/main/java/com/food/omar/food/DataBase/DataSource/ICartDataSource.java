package com.food.omar.food.DataBase.DataSource;

import com.food.omar.food.DataBase.ModelDB.Cart;

import java.util.List;

import io.reactivex.Flowable;

public interface ICartDataSource {
    Flowable<List<Cart>> getCartItem();
    void emptyCart();
    int countCartItems();
    float sumCartItems();

    void insertToCart(Cart...carts);
    void updateToCart(Cart...carts);
    void deleteCartItem(Cart cart);
    Flowable<List<Cart>> getCartItemByID(int cartItemID);

}
