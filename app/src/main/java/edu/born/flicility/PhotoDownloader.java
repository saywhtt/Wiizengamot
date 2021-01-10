package edu.born.flicility;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import edu.born.flicility.adapters.PhotoAdapter;

public class PhotoDownloader extends HandlerThread {

    private static final String TAG = PhotoDownloader.class.getCanonicalName();
    private static final int MESSAGE_DOWNLOAD = 0;

    private boolean mHasQuit = false;
    private Handler mRequestHandler;
    private Handler mResponseHandler;

    private ConcurrentMap<PhotoAdapter.PhotoHolder, String> mRequestMap = new ConcurrentHashMap<>();
    private PhotoDownloadListener mPhotoDownloadListener;

    public void setPhotoDownloadListener(PhotoDownloadListener listener) {
        mPhotoDownloadListener = listener;
    }

    public PhotoDownloader(Handler responseHandler) {
        super(TAG);
        mResponseHandler = responseHandler;
    }

    @Override
    public boolean quit() {
        mHasQuit = true;
        return super.quit();
    }

    @Override
    protected void onLooperPrepared() {
        mRequestHandler = new Handler(getLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                if (msg.what == MESSAGE_DOWNLOAD) {
                    handleRequest((PhotoAdapter.PhotoHolder) msg.obj);
                }
            }
        };
    }

    public void queueThumbnail(PhotoAdapter.PhotoHolder holder, String url) {

        if (url == null) {
            mRequestMap.remove(holder);
        } else {
            mRequestMap.put(holder, url);
            mRequestHandler.obtainMessage(MESSAGE_DOWNLOAD, holder)
                    .sendToTarget();
        }
    }

    public void clearQueue() {
        mRequestHandler.removeMessages(MESSAGE_DOWNLOAD);
        mRequestMap.clear();
    }


    private void handleRequest(final PhotoAdapter.PhotoHolder holder) {
        try {
            final String url = mRequestMap.get(holder);

            if (url == null) return;

            byte[] bitmapBytes = new FlickrFetchr().getBytesByUrl(url);
            final Bitmap bitmap = BitmapFactory
                    .decodeByteArray(bitmapBytes, 0, bitmapBytes.length);

            mResponseHandler.post(() -> {
                if (!Objects.equals(mRequestMap.get(holder), url) | mHasQuit) return;
                mRequestMap.remove(holder);
                mPhotoDownloadListener.onPhotoDownloaded(holder, bitmap);
            });

        } catch (IOException ignored) {

        }
    }
}
