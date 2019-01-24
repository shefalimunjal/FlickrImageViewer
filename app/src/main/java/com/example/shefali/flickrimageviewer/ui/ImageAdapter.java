package com.example.shefali.flickrimageviewer.ui;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.shefali.flickrimageviewer.R;
import com.example.shefali.flickrimageviewer.database.Image;

import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.RecyclerView;

public class ImageAdapter extends PagedListAdapter<Image, ImageAdapter.ViewHolder> {

    private static final String TAG = ImageAdapter.class.getSimpleName();

    private final LayoutInflater layoutInflater;

    public ImageAdapter(Context context) {
        super(Image.DIFF_CALLBACK);
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.image_grid_view_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Image image = getItem(position);
        if (image != null) {
            holder.bind(image);
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageView;

        ViewHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.image);
        }

        void bind(Image image) {
            String url = image.getThumbnailUrl();
            Log.d(TAG, "loading image: " + url);

            // In Glide v4, we need to use options for center cropping.
            RequestOptions options = new RequestOptions();
            options = options.centerCrop();
            options = options.placeholder(R.color.lightGray);

            Glide.with(imageView)
                    .load(url)
                    .apply(options)
                    .into(imageView);
        }
    }
}
