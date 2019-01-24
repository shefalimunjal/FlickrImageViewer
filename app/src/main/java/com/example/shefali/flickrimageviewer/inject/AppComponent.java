package com.example.shefali.flickrimageviewer.inject;

import com.android.volley.RequestQueue;
import com.example.shefali.flickrimageviewer.ui.ImageViewerActivity;
import com.example.shefali.flickrimageviewer.ui.ImageViewerViewModel;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {
    void inject(ImageViewerViewModel viewerViewModel);
}
