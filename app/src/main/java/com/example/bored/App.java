package com.example.bored;

import android.app.Application;
import android.content.Context;

public class App extends Application {

    public static App instance;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        instance = this;
    }



}
