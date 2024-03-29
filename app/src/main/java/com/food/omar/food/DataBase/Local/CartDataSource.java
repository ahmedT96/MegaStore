package com.food.omar.food.DataBase.Local;

import com.food.omar.food.DataBase.DataSource.ICartDataSource;
import com.food.omar.food.DataBase.ModelDB.Cart;

import java.util.List;

import io.reactivex.Flowable;

public class CartDataSource implements ICartDataSource {

    private CartDAO cartDAO;
    private static CartDataSource instance;

    public CartDataSource(CartDAO cartDAO) {
        this.cartDAO = cartDAO;
    }
public static CartDataSource getInstance(CartDAO cartDAO)
{
    if (instance==null)
        instance=new CartDataSource(cartDAO);
    return instance;
}
    @Override
    public Flowable<List<Cart>> getCartItem() {
        return cartDAO.getCartItem();
    }

    @Override
    public void emptyCart() {
cartDAO.emptyCart();
    }

    @Override
    public int countCartItems() {
        return cartDAO.countCartItems();
    }

    @Override
    public float sumCartItems() {
        return cartDAO.sumCartItems();
    }

    @Override
    public void insertToCart(Cart... carts) {
        cartDAO.insertToCart(carts);
    }

    @Override
    public void updateToCart(Cart... carts) {
cartDAO.updateToCart(carts);
    }

    @Override
    public void deleteCartItem(Cart cart) {
cartDAO.deleteCartItem(cart);
    }

    @Override
    public Flowable<List<Cart>> getCartItemByID(int cartItemID) {
        return null;
    }
}
