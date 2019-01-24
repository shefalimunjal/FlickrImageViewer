package com.example.shefali.flickrimageviewer.ui;

import androidx.lifecycle.LiveData;

import com.example.shefali.flickrimageviewer.ImageViewerApplication;
import com.example.shefali.flickrimageviewer.database.Image;
import com.example.shefali.flickrimageviewer.database.ImageRepository;

import javax.inject.Inject;

import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

public class ImageViewerViewModel extends ViewModel {

    @Inject ImageRepository repository;

    private final LiveData<PagedList<Image>> liveImages;

    public ImageViewerViewModel() {
        ImageViewerApplication.getAppComponent().inject(this);

        PagedList.Config pagedListConfig =
                (new PagedList.Config.Builder())
                        .setPrefetchDistance(50)
                        .setPageSize(100)
                        .setEnablePlaceholders(true)
                        .build();

        liveImages = (new LivePagedListBuilder<Integer, Image>(repository.getImagesFactory(), pagedListConfig)).build();
    }
    public LiveData<PagedList<Image>> getLiveImages() {
        return liveImages;
    }

    public LiveData<ImageRepository.FetchStatus> isFetchingData() {
        return repository.isFetching();
    }

    public void fetchNewPageIfNeeded() {
        repository.fetchNewPageIfNeeded();
    }
}
