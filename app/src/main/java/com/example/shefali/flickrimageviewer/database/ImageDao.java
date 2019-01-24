package com.example.shefali.flickrimageviewer.database;

import java.util.List;

import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface ImageDao {
    @Insert(onConflict = REPLACE)
    void saveImages(List<Image> images);

    @Query("SELECT * FROM " + Image.TABLE_NAME + " ORDER BY localId ASC")
    DataSource.Factory<Integer, Image> getImagesFactory();
}
