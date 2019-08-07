package com.food.omar.food.DataBase.Local;

import com.food.omar.food.DataBase.DataSource.IFavoritRespository;
import com.food.omar.food.DataBase.ModelDB.Favorite;

import java.util.List;

import io.reactivex.Flowable;

public class FavoritDataSource implements IFavoritRespository {

    private FavoritDAO favoritDAO;
    private static FavoritDataSource instance;

    public FavoritDataSource(FavoritDAO favoritDAO) {
        this.favoritDAO = favoritDAO;
    }
    public static FavoritDataSource getInstance(FavoritDAO favoritDAO)
    {
        if (instance==null)
            instance=new FavoritDataSource(favoritDAO);
        return instance;
    }


    @Override
    public Flowable<List<Favorite>> getFavtItem() {
        return favoritDAO.getFavtItem();
    }

    @Override
    public int isFavoritDAO(int FavItemID) {
        return favoritDAO.isFavoritDAO(FavItemID);

    }

    @Override
    public void deleteFavItem(Favorite cart) {
        favoritDAO.deleteFavItem(cart);

    }

    @Override
    public void insertToFav(Favorite... cart) {
        favoritDAO.insertToFav(cart);

    }



}


