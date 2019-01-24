package com.example.shefali.flickrimageviewer.database;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.shefali.flickrimageviewer.network.VolleyNetworkManager;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executor;

import javax.inject.Inject;
import javax.inject.Singleton;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.LivePagedListBuilder;

/**
 * Repository class to manage images in database and from network.
 */
@Singleton
public class ImageRepository {

    private static final String TAG = ImageRepository.class.getSimpleName();
    private static final String FLICKR_API_KEY = "7b85e389607020e3b5a12c5a40e260db";

    private final AppDatabase database;
    private final SharedPreferencesManager sharedPreferencesManager;
    private final VolleyNetworkManager volleyNetworkManager;
    private final Gson gson;
    private final Executor executor;

    public enum FetchStatus{
        FETCHING,
        DONE,
        FAILED;
    }
    private final MutableLiveData<FetchStatus> isFetching;

    @Inject
    public ImageRepository(AppDatabase database,
                           SharedPreferencesManager sharedPreferencesManager,
                           VolleyNetworkManager volleyNetworkManager,
                           Gson gson,
                           Executor executor) {
        this.database = database;
        this.sharedPreferencesManager = sharedPreferencesManager;
        this.volleyNetworkManager = volleyNetworkManager;
        this.gson = gson;
        this.executor = executor;
        isFetching = new MutableLiveData<FetchStatus>();
        isFetching.setValue(FetchStatus.DONE);
    }

    public DataSource.Factory<Integer, Image> getImagesFactory() {
        return database.getImageDao().getImagesFactory();
    }

    public LiveData<FetchStatus> isFetching() {
        return isFetching;
    }

    public void fetchNewPageIfNeeded() {
        if ((isFetching.getValue() != FetchStatus.FETCHING) && isNewPageAvailableOnServer()) {
            fetchImages(sharedPreferencesManager.getLastFetchedPageNo() + 1);
        }
    }

    private boolean isNewPageAvailableOnServer() {
        int lastFetchedPage = sharedPreferencesManager.getLastFetchedPageNo();
        int totalPages = sharedPreferencesManager.getTotalPages();
        return (totalPages == SharedPreferencesManager.DEF_TOTAL_PAGES) || lastFetchedPage < totalPages;
    }

    private void fetchImages(int page) {
        final String url = String.format(
                Locale.US,
                "https://api.flickr.com/services/rest/?method=flickr.photos.getRecent&api_key=%s&page=%d&format=json&nojsoncallback=1",
                FLICKR_API_KEY,
                page);

        Log.d(TAG, "fetching url: " + url);
        final JsonObjectRequest request = new JsonObjectRequest(
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(TAG, "response received from the server");
                        try {
                            // Update isFetching to false, so that the activity can stop loading.
                            isFetching.setValue(FetchStatus.DONE);

                            // Get values from json response
                            String imagesJsonString = response.getJSONObject("photos").getJSONArray("photo").toString();
                            int totalPages = response.getJSONObject("photos").getInt("pages");
                            int perPageCount = response.getJSONObject("photos").getInt("perpage");

                            // Convert json data to java objects using gson.
                            Type listType = new TypeToken<List<Image>>() {}.getType();
                            final List<Image> images = gson.fromJson(imagesJsonString, listType);

                            // Save images to database on background thread. Room doesn't allow queries on main thread.
                            executor.execute(new Runnable() {
                                @Override
                                public void run() {
                                    database.getImageDao().saveImages(images);
                                    Log.i(TAG, "number of images saved in database: " + images.size());
                                }
                            });

                            // Update page counts in shared preferences.
                            sharedPreferencesManager.incrementLastFetchedPageNo();
                            sharedPreferencesManager.setTotalPages(totalPages);
                            sharedPreferencesManager.setPerPageCount(perPageCount);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        isFetching.setValue(FetchStatus.FAILED);
                        Log.e(TAG, "failed to fetch the response from url: " + url, error);
                    }
                });

        isFetching.setValue(FetchStatus.FETCHING);
        volleyNetworkManager.addToRequestQueue(request);
    }
}
