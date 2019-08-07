package com.food.omar.food.Retrofit;

import com.food.omar.food.Model.ChackUserResponse;
import com.food.omar.food.Model.Drink;
import com.food.omar.food.Model.Token;
import com.food.omar.food.Model.User;
import com.food.omar.food.Model.banners;
import com.food.omar.food.Model.Category;
import com.food.omar.food.Model.orders;


import java.util.List;
import java.util.Locale;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface IFoodApi {
    @FormUrlEncoded
    @POST("checkUser.php")
    Call<ChackUserResponse> chackUserResponse(@Field("phone")String phone);

    @FormUrlEncoded
    @POST("Register.php")
    Call<User> register(
            @Field("phone")String phone,
            @Field("name")String name,
            @Field("birthdate")String birthdate,
            @Field("address")String adress);

    @FormUrlEncoded
    @POST("getUser.php")
    Call<User> getUserInformation(
            @Field("phone")String phone);

    @FormUrlEncoded
    @POST("getdrink.php")
    Observable<List<Drink>> getdrink(
            @Field("id")String id);

    @GET("getProducts.php")
    Observable<List<Drink>> getProducts();


    @GET("getBanners.php")
    Observable<List<banners>> getBanners();


    @GET("getCategory.php")
    Observable<List<Category>> getMenu();
    @Multipart
    @POST("ubdate.php")
    Call<String>uploasFile(@Part MultipartBody.Part phone, @Part MultipartBody.Part file );



    @FormUrlEncoded
    @POST("submitOrder.php")
    Call<String> submitOrder(
    @Field("orderDitails")String orderDitails,
    @Field("orderPhone")String orderPhone,
    @Field("orderAdrdess")String orderAdrdess,
    @Field("orderCommunt")String orderCommunt,
    @Field("orderPrice")float orderPrice);

    @FormUrlEncoded
    @POST("ubdateToken.php")
    Call<User> ubdateToken(@Field("phone")String phone ,  @Field("token")String token,@Field("IsServerToken")String IsServerToken);

    @FormUrlEncoded
    @POST("getdrink.php")
    Observable<List<orders>> getOrders(
            @Field("phone")String phone,
    @Field("statues")String statues);

    @FormUrlEncoded
    @POST("getToken.php")
    Call<Token> getToken(@Field("phone")String phone ,
                         @Field("IsServerToken")String IsServerToken);

}
