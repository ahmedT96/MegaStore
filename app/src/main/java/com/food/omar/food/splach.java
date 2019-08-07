package com.food.omar.food;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gigamole.library.PulseView;

public class splach extends AppCompatActivity {
    private static int splash_time_out=4000;
    PulseView logo;

    Animation anmi;
     TextView load;
    boolean connected = false;
    ConnectivityManager connectivityManager;
    Button buttonConect;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splach);
        getSupportActionBar().hide();
         load= (TextView) findViewById(R.id.load);
         logo=  findViewById(R.id.imgee);
         buttonConect=  findViewById(R.id.buttonConect);
        buttonConect.setVisibility(View.INVISIBLE);
        connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        }
        else
            connected = false;


       logo.startPulse();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (connected) {
                    Intent homeintent = new Intent(splach.this, Home.class);
                    //homeactivity name of second layout.java
                    startActivity(homeintent);
                    finish();
                }
                else {
                    load.setText("No internet Connection");
                    buttonConect.setVisibility(View.VISIBLE);
                }

            }
        },splash_time_out);

    }

    public void cloked(View view) {
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            Intent homeintent = new Intent(splach.this, Home.class);
            //homeactivity name of second layout.java
            startActivity(homeintent);
            finish();
        }

    }
}
