package com.example.shefali.flickrimageviewer.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Image.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ImageDao getImageDao();
}
