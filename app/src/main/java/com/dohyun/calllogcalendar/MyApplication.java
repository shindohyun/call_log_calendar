package com.dohyun.calllogcalendar;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import androidx.core.os.HandlerCompat;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyApplication extends Application {
    private static int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();
    public ExecutorService executorService = Executors.newFixedThreadPool(NUMBER_OF_CORES);
    public Handler mainThreadHandler = HandlerCompat.createAsync(Looper.getMainLooper());
    public CallLogManager callLogManager = new CallLogManager();

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
