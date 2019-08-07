package com.food.omar.food.DataBase.DataSource;


import com.food.omar.food.DataBase.ModelDB.Favorite;

import java.util.List;

import io.reactivex.Flowable;

public interface IFavoritRespository {
    Flowable<List<Favorite>> getFavtItem();

    int isFavoritDAO(int FavItemID);
    void deleteFavItem(Favorite cart);
    void insertToFav(Favorite...cart);

}
