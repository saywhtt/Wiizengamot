package edu.born.flicility;

import android.graphics.Bitmap;

import edu.born.flicility.adapters.PhotoAdapter;

public interface PhotoDownloadListener {
    void onPhotoDownloaded(PhotoAdapter.PhotoHolder holder, Bitmap bitmap);
}
