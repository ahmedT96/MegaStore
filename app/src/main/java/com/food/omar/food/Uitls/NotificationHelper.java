package com.food.omar.food.Uitls;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.food.omar.food.R;

public class NotificationHelper extends ContextWrapper {
    private  static final String CHENAL_ID="omar.dev.drink";
    private  static final String CHENAL_Name="Drink Shop";

    private NotificationManager notificationManager;

public NotificationHelper(Context base)
{
    super(base);
    if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
        createChanel();

}

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createChanel() {
        NotificationChannel edmtChannel= new NotificationChannel(CHENAL_ID,CHENAL_Name,NotificationManager.IMPORTANCE_DEFAULT);
        edmtChannel.enableLights(false);
        edmtChannel.enableVibration(true);
        edmtChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        getManger().createNotificationChannel(edmtChannel);
    }

    public  NotificationManager getManger() {
        if (notificationManager==null)
            notificationManager= (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        return notificationManager;
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public Notification.Builder getDrinkShop(String title, String message , Uri soundUri)
    {
        return new Notification.Builder(getApplicationContext(),CHENAL_ID)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setSound(soundUri)
                .setAutoCancel(true);
    }
}
