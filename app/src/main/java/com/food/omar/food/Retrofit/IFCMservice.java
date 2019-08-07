package com.food.omar.food.Retrofit;

import com.food.omar.food.Model.DataMessage;
import com.food.omar.food.Model.myResponce;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface IFCMservice {
    @Headers({
            "Content-type :application/json",
            "Authorization:Key=AAAACSA-EkQ:APA91bFvN3CAfLf0KcijzDa4RCPZr_J2Fqa3wVmyoLHNIhYb38kAm_-I7SnvBjgZ-1m3e05_1imwWo4KXCnJXFqAU8ge1vVlGb4ckKAxcICf7_oYCtSf0i1bDJGWeNy6L1JB6UYeRAV8"
    }
    )
    @POST("form/send")
    Call<myResponce> sendNotification(@Body DataMessage body) ;
}
