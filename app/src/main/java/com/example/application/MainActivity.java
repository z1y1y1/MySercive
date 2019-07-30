package com.example.application;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuWrapperFactory;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class MainActivity extends AppCompatActivity {

    Handler handler = new Handler(){};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        NotificationManagerUtils.startNotificationManager(this,"adadadasdas",R.mipmap.ic_launcher);
        Message message = new Message();
        message.obj = "123";
        handler.sendMessageDelayed(message,2000);
        Intent intent = new Intent(this, MyService.class);
        startService(intent);
    }



    @Override
    protected void onStop() {
        super.onStop();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

//        startService(new Intent(MainActivity.this,MyService.class));
            }
        });
    }
}
