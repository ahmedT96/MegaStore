package com.food.omar.food.DataBase.DataSource;

import com.food.omar.food.DataBase.ModelDB.Cart;

import java.util.List;

import io.reactivex.Flowable;

public class CartRepository implements ICartDataSource {
    private ICartDataSource iCartDataSource;

    public CartRepository(ICartDataSource iCartDataSource) {
        this.iCartDataSource = iCartDataSource;
    }
    public static CartRepository instance;

    public static CartRepository getInstance(ICartDataSource iCartDataSource) {
        if (instance==null)
        {
            instance=new CartRepository(iCartDataSource);
        }
        return instance;
    }

    @Override
    public Flowable<List<Cart>> getCartItem() {

        return iCartDataSource.getCartItem();
    }

    @Override
    public void emptyCart() {
        iCartDataSource.emptyCart();
    }

    @Override
    public int countCartItems() {
        return iCartDataSource.countCartItems();
    }

    @Override
    public float sumCartItems() {
        return iCartDataSource.sumCartItems();
    }

    @Override
    public void insertToCart(Cart... carts) {
        iCartDataSource.insertToCart(carts);
    }

    @Override
    public void updateToCart(Cart... carts) {
        iCartDataSource.updateToCart(carts);

    }

    @Override
    public void deleteCartItem(Cart cart) {
        iCartDataSource.deleteCartItem(cart);
    }

    @Override
    public Flowable<List<Cart>> getCartItemByID(int cartItemID) {
        return iCartDataSource.getCartItemByID(cartItemID);
    }
}
