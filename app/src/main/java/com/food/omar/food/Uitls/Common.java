package com.food.omar.food.Uitls;

import android.widget.Switch;

import com.food.omar.food.DataBase.DataSource.CartRepository;
import com.food.omar.food.DataBase.DataSource.FavoritRespository;
import com.food.omar.food.DataBase.Local.CartDatabase;
import com.food.omar.food.Model.Category;
import com.food.omar.food.Model.Drink;
import com.food.omar.food.Model.User;
import com.food.omar.food.Retrofit.FCMClient;
import com.food.omar.food.Retrofit.IFCMservice;
import com.food.omar.food.Retrofit.IFoodApi;
import com.food.omar.food.Retrofit.RetrofitCleint;

import java.util.ArrayList;
import java.util.List;

public class Common {
    public static final String BASE_URL="http://megatronics.io/Mobile/ABI/";
    public static User currentuser=null;
    public static Category currentCategory=null;
    public static String currenttoping="7";
    public static String currenfinalComment=" ";
private static final String FCM_API="https://form.googleapis.com/";
public static IFCMservice getFCMService()
{
    return FCMClient.getClient(FCM_API).create(IFCMservice.class);
}

    public static IFoodApi getAPI()
    {
        return RetrofitCleint.getClint(BASE_URL).create(IFoodApi.class);
    }

    public static CartDatabase cartDatabase;
    public static CartRepository cartRepository;
    public static FavoritRespository favoritRespository;


    public static int sizes=-1;
    public static int colors=-1;

    public static List<Drink>topinglist=new ArrayList<>();

    public static String convertCodeToStatue(int orderState) {
        switch (orderState)
        {
            case 0 :return "placed";
            case 1 :return "Processing";
            case 2 :return "Shipping";
            case 3 :return "Shipped";
            case -1 :return "Cancelled";
        }
        return "Order Error";
    }
}
