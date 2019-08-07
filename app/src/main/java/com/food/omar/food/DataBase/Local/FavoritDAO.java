package com.food.omar.food.DataBase.Local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.food.omar.food.DataBase.ModelDB.Favorite;


import java.util.List;

import io.reactivex.Flowable;
@Dao
public interface FavoritDAO
{

    @Query("SELECT * FROM Favorite")
    Flowable<List<Favorite>> getFavtItem();

    @Query("SELECT EXISTS (SELECT 1 FROM Favorite WHERE id=:FavItemID)")
    int isFavoritDAO(int FavItemID);
    @Delete
    void deleteFavItem(Favorite cart);
    @Insert
    void insertToFav(Favorite...cart);
}
