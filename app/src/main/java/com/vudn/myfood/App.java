package com.vudn.myfood;

import android.app.Application;

public class App extends Application {
    static App wifiInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        wifiInstance = this;
    }

    public static synchronized App getInstance() {
        return wifiInstance;
    }
}
