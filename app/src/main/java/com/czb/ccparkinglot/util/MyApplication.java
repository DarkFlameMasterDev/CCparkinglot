package com.czb.baidumap;

import android.app.Application;

public class MyApplication extends Application {
    private static MyApplication app;

    public static MyApplication getContext() {
        return app;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
    }
}
