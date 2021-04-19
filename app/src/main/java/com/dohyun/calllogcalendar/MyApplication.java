package com.dohyun.calllogcalendar;

import android.app.Application;

public class MyApplication extends Application {
    public CallLogManager callLogManager;

    @Override
    public void onCreate() {
        super.onCreate();

        callLogManager = new CallLogManager();
    }
}
