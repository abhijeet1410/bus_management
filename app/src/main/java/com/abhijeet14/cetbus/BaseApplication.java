package com.abhijeet14.cetbus;

import android.app.Application;

import com.google.firebase.FirebaseApp;

public class BaseApplication extends Application {
    public BaseApplication() {
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
    }
}
