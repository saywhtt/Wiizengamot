package edu.born.flicility.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.fragment.app.Fragment;

import edu.born.flicility.fragments.PhotoGalleryFragment;

public class PhotoGalleryActivity extends AbstractFragmentActivity {
    @Override
    protected Fragment getFragment() {
        return new PhotoGalleryFragment();
    }

    public static Intent newIntent(Context context) {
        return new Intent(context, PhotoGalleryActivity.class);
    }
}
