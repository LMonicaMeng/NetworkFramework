package com.btime.networkframework;

import android.app.Application;

import com.btime.filedownload.file.FileStorageManager;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FileStorageManager.getInstance().init(this);
    }
}
