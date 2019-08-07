package com.food.omar.food.DataBase.DataSource;

import com.food.omar.food.DataBase.Local.FavoritDAO;
import com.food.omar.food.DataBase.Local.FavoritDataSource;
import com.food.omar.food.DataBase.ModelDB.Cart;
import com.food.omar.food.DataBase.ModelDB.Favorite;

import java.util.List;

import io.reactivex.Flowable;

public class FavoritRespository implements IFavoritRespository{
   private IFavoritRespository favoritRespository;

    private static FavoritRespository instance;

    public static FavoritRespository getInstance(IFavoritRespository favoritRespository)
    {
        if (instance==null)
            instance=new FavoritRespository(favoritRespository);
        return instance;
    }

    public FavoritRespository(IFavoritRespository favoritRespository) {
        this.favoritRespository = favoritRespository;
    }


    @Override
    public Flowable<List<Favorite>> getFavtItem() {
        return favoritRespository.getFavtItem();
    }

    @Override
    public int isFavoritDAO(int FavItemID) {
        return favoritRespository.isFavoritDAO(FavItemID);
    }

    @Override
    public void deleteFavItem(Favorite cart) {
        favoritRespository.deleteFavItem(cart);
    }

    @Override
    public void insertToFav(Favorite... cart) {
        favoritRespository.insertToFav(cart);
    }


}
