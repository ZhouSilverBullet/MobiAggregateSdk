package com.mobi.core;

import android.app.Fragment;
import android.util.Log;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/6/3 20:59
 * @Dec 略
 */
public class AdFragment extends Fragment {
    public static final String TAG = "AdFragment";

    public AdFragment() {
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e(TAG, "onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e(TAG, "onResume");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy");
    }
}
