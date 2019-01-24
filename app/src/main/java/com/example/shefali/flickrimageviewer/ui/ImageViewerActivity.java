package com.example.shefali.flickrimageviewer.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.shefali.flickrimageviewer.R;
import com.example.shefali.flickrimageviewer.database.Image;
import com.example.shefali.flickrimageviewer.database.ImageRepository;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class ImageViewerActivity extends AppCompatActivity {
    private static final String TAG = ImageViewerActivity.class.getSimpleName();

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ConstraintLayout retryLayout;
    private ImageAdapter adapter;
    private ImageViewerViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_viewer);

        initUi();
        initObservers();
        initListeners();

        // Fetch first batch of images for the first time.
        viewModel.fetchNewPageIfNeeded();
    }

    private void initUi() {
        // Init elements
        swipeRefreshLayout = findViewById(R.id.image_list_refresh_layout);
        recyclerView = findViewById(R.id.image_list);
        retryLayout = findViewById(R.id.retry_layout);

        // Set adapter and layoutmanager on recyclerview.
        adapter = new ImageAdapter(this);
        recyclerView.setAdapter(adapter);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(5, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);


    }

    private void initObservers() {
        // Get the view model.
        viewModel = ViewModelProviders.of(this).get(ImageViewerViewModel.class);

        // Observe for new pages from the database and refresh adapter.
        viewModel.getLiveImages().observe(this, new Observer<PagedList<Image>>() {
            @Override
            public void onChanged(PagedList<Image> imagesPagedList) {
                adapter.submitList(imagesPagedList);
            }
        });

        // Observer for fetching status to update the loading the indicator.
        viewModel.isFetchingData().observe(this, new Observer<ImageRepository.FetchStatus>() {
            @Override
            public void onChanged(ImageRepository.FetchStatus fetchStatus) {
                if (fetchStatus == ImageRepository.FetchStatus.FETCHING) {
                    swipeRefreshLayout.setRefreshing(true);
                    retryLayout.setVisibility(View.GONE);

                } else if (fetchStatus == ImageRepository.FetchStatus.DONE) {
                    swipeRefreshLayout.setRefreshing(false);
                    retryLayout.setVisibility(View.GONE);

                } else if (fetchStatus == ImageRepository.FetchStatus.FAILED) {
                    swipeRefreshLayout.setRefreshing(false);
                    retryLayout.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void initListeners() {
        // Pull to refresh is not supported, so hide the loader immediately.
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        // Set scroll listener on recyclerview, and fetch more data when scrolled to end.
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (!recyclerView.canScrollVertically(1)) {
                    Log.d(TAG, "end of list reached in recycler view, may be load more from server.");
                    viewModel.fetchNewPageIfNeeded();
                }
            }
        });

        // Set click listener on retry layout.
        retryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.fetchNewPageIfNeeded();
            }
        });
    }
}
