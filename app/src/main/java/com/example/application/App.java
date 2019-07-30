package com.example.application;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        startService(new Intent(this, MyService.class));
    }

    @Override
    public void onTerminate() {        // 程序终止的时候执行
        Log.e("APP", "onTerminate");
        Intent intent = new Intent(this, MyService.class);
        startService(intent);
        super.onTerminate();
    }

    @Override
    public void onLowMemory() {        // 低内存的时候执行
        Log.e("APP", "onLowMemory");
        super.onLowMemory();
    }

    @Override
    public void onTrimMemory(int level) {// 程序在内存清理的时候执行
        Log.e("APP", "onTrimMemory"+level);
        /*Intent intent = new Intent(this, MyService.class);
        startService(intent);*/
        super.onTrimMemory(level);
    }


}
