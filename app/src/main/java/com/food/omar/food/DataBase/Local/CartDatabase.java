package com.food.omar.food.DataBase.Local;


import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.DatabaseConfiguration;
import android.arch.persistence.room.InvalidationTracker;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;

import com.food.omar.food.DataBase.ModelDB.Cart;
import com.food.omar.food.DataBase.ModelDB.Favorite;

@Database(entities = {Cart.class, Favorite.class},version = 1,exportSchema = false)
public abstract class CartDatabase extends RoomDatabase{

  public abstract CartDAO cartDAO();
    public abstract FavoritDAO favoritDAO();

    public static CartDatabase instance;

    public static CartDatabase getInstance(Context context)
    {
        if (instance==null)
            instance= Room.databaseBuilder(context,CartDatabase.class,"Food_V1")
                    .allowMainThreadQueries()
                    .build();
        return instance;
    }
}
