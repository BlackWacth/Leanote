package com.bruce.leanote.global;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

/**
 * Created by Bruce on 2017/3/30.
 */

public class LApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        LeakCanary.install(this);
    }
}
