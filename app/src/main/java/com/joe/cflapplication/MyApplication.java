package com.joe.cflapplication;

import android.app.Application;

/**
 * @Autor zongdongdong on 2016/9/29.
 */

public class MyApplication extends Application {
    public static MyApplication instance;

    public static MyApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
}
