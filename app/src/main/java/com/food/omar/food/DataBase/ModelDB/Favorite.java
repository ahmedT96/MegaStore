package com.food.omar.food.DataBase.ModelDB;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import io.reactivex.annotations.NonNull;

@Entity(tableName = "Favorite")
public class Favorite {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public int id;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "Link")
    public String Link;


    @ColumnInfo(name = "price")
    public double price;


    @ColumnInfo(name = "toppingExtera")
    public String menuID;
}
