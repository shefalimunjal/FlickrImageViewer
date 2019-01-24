package com.example.shefali.flickrimageviewer.database;

import java.util.Locale;
import androidx.annotation.NonNull;

import androidx.recyclerview.widget.DiffUtil;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = Image.TABLE_NAME)
public class Image {
    static final String TABLE_NAME = "image";

    @PrimaryKey(autoGenerate = true)
    private int localId;
    private String id;
    private String server;
    private String secret;
    private int farm;

    public int getLocalId() {
        return localId;
    }

    public void setLocalId(int localId) {
        this.localId = localId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public int getFarm() {
        return farm;
    }

    public void setFarm(int farm) {
        this.farm = farm;
    }

    public String getThumbnailUrl() {
        return String.format(
                Locale.US,
                "https://farm%d.staticflickr.com/%s/%s_%s_t.jpg", farm, server, id, secret
        );
    }

    public static final DiffUtil.ItemCallback<Image> DIFF_CALLBACK = new DiffUtil.ItemCallback<Image>() {
        @Override
        public boolean areItemsTheSame(Image oldImage, Image newImage) {
            return oldImage.getId().equals(newImage.getId());
        }

        @Override
        public boolean areContentsTheSame(Image oldImage, Image newImage) {
            return oldImage.equals(newImage);
        }
    };
}
