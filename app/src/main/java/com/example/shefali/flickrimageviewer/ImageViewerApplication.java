package com.example.shefali.flickrimageviewer;

import android.app.Application;

import com.example.shefali.flickrimageviewer.inject.AppComponent;
import com.example.shefali.flickrimageviewer.inject.AppModule;
import com.example.shefali.flickrimageviewer.inject.DaggerAppComponent;

public class ImageViewerApplication extends Application {

    private static AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        appComponent = DaggerAppComponent
                .builder()
                .appModule(new AppModule(this))
                .build();
    }

    public static AppComponent getAppComponent() {
        return appComponent;
    }
}
