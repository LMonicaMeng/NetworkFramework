package com.btime.networkframework;

import android.app.Application;

import com.btime.filedownload.file.FileStorageManager;
import com.btime.filedownload.http.DownloadManager;
import com.btime.filedownload.http.HttpManager;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FileStorageManager.getInstance().init(this);
        HttpManager.getInstance().init(this);
    }
}
