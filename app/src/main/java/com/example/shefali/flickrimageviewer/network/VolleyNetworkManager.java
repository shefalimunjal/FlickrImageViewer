package com.example.shefali.flickrimageviewer.network;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class VolleyNetworkManager {
    private final RequestQueue requestQueue;

    @Inject
    public VolleyNetworkManager(RequestQueue requestQueue) {
        this.requestQueue = requestQueue;
    }

    public void addToRequestQueue(Request request) {
        requestQueue.add(request);
    }

}
