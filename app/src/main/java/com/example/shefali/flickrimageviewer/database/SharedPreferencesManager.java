package com.example.shefali.flickrimageviewer.database;

import android.content.SharedPreferences;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class SharedPreferencesManager {
    private static final String LAST_FETCHED_PAGE_KEY = "last_fetched_page_no";
    private static final String TOTAL_PAGES_KEY = "total_pages";
    private static final String PER_PAGE_COUNT_KEY = "per_page_count";
    public static final int DEF_LAST_PAGE_VAL = 0 ;
    public static final int DEF_TOTAL_PAGES = -1 ;
    public static final int DEF_PER_PAGE_COUNT = -1 ;

    private final SharedPreferences sharedPreferences;

    @Inject
    public SharedPreferencesManager(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;

    }

    public int getLastFetchedPageNo() {
        return sharedPreferences.getInt(LAST_FETCHED_PAGE_KEY, DEF_LAST_PAGE_VAL);
    }

    public int getTotalPages() {
        return sharedPreferences.getInt(TOTAL_PAGES_KEY, DEF_TOTAL_PAGES);
    }

    public int getPerPageCount() {
        return sharedPreferences.getInt(PER_PAGE_COUNT_KEY, DEF_PER_PAGE_COUNT);
    }

    public void incrementLastFetchedPageNo() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(LAST_FETCHED_PAGE_KEY, getLastFetchedPageNo() + 1);
        editor.apply();
    }

    public void setTotalPages(int totalPages) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(TOTAL_PAGES_KEY, totalPages);
        editor.apply();
    }

    public void setPerPageCount(int perPageCount) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(PER_PAGE_COUNT_KEY, perPageCount);
        editor.apply();
    }
}
