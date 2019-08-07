package com.food.omar.food.Service;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.food.omar.food.Model.User;
import com.food.omar.food.R;
import com.food.omar.food.Retrofit.IFoodApi;
import com.food.omar.food.Uitls.Common;
import com.food.omar.food.Uitls.NotificationHelper;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyFirebaseMesseging extends FirebaseMessagingService {

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        if (Common.currentuser!=null)
        {
            updateTokenTofirebase(s);
        }
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
            sendNotificationABI25(remoteMessage);
        else
            sendNotification(remoteMessage);

    }

    private void sendNotification(RemoteMessage remoteMessage) {
        Map<String,String> data=remoteMessage.getData();
        String title =data.get("title");
        String message=data.get("message");

        Uri dufaltsondUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder =new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(dufaltsondUri);
        NotificationManager noti = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        noti.notify(new Random().nextInt(),builder.build());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void sendNotificationABI25(RemoteMessage remoteMessage) {
        Map<String,String> data=remoteMessage.getData();
        String title =data.get("title");
        String message=data.get("message");
        NotificationHelper helper;
        Notification.Builder builder;

        Uri dufaltsondUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        helper=new NotificationHelper(this);
        builder=helper.getDrinkShop(title,message,dufaltsondUri);
        helper.getManger().notify(new Random().nextInt(),builder.build());


    }

    private void updateTokenTofirebase(String token) {
        IFoodApi mService= Common.getAPI();
        mService.ubdateToken(Common.currentuser.getPhone(),token,"0").enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                Log.d("debug",response.toString());
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d("debug",t.toString());

            }
        });
    }

}
