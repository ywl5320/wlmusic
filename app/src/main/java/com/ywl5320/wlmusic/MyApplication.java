package com.ywl5320.wlmusic;

import android.app.Application;

/**
 * Created by ywl on 2018-1-13.
 */

public class MyApplication extends Application{

    private static MyApplication instance;
    private Long token;

    public static MyApplication getInstance()
    {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public String getToken() {
        return String.valueOf(token);
    }

    public void setToken(Long token) {
        this.token = token;
    }
}
