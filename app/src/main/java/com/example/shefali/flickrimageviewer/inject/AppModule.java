package com.example.shefali.flickrimageviewer.inject;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.shefali.flickrimageviewer.database.AppDatabase;
import com.google.gson.Gson;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Singleton;

import androidx.room.Room;
import dagger.Module;
import dagger.Provides;

import static android.content.Context.MODE_PRIVATE;

@Module
public class AppModule {
    private final Context context;

    public AppModule(Context context) {
        this.context = context;
    }

    @Provides
    @Singleton
    public Context provideApplicationContext() {
        return context;
    }

    @Provides
    @Singleton
    public AppDatabase provideAppDatabase(Context context) {
        return Room.databaseBuilder(context, AppDatabase.class, "images.db").build();
    }

    @Provides
    @Singleton
    public RequestQueue provideVolleyRequestQueue(Context context) {
        return Volley.newRequestQueue(context);
    }

    @Provides
    @Singleton
    public Gson provideGson() {
        return new Gson();
    }

    @Provides
    @Singleton
    public SharedPreferences provideSharedPreferences(Context context) {
        return context.getSharedPreferences("app.prefs", MODE_PRIVATE);
    }

    @Provides
    @Singleton
    public Executor provideExecutor() {
        return Executors.newSingleThreadExecutor();
    }
}
