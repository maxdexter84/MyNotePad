package com.maxdexter.mynote;

import android.app.Application;
import android.support.v7.app.AppCompatDelegate;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.getDefaultNightMode());
    }
}
