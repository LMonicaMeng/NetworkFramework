package com.btime.networkframework;

import android.app.Application;

import com.btime.filedownload.db.DownloadHelper;
import com.btime.filedownload.file.FileStorageManager;
import com.btime.filedownload.http.DownloadManager;
import com.btime.filedownload.http.HttpManager;
import com.facebook.stetho.Stetho;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FileStorageManager.getInstance().init(this);
        HttpManager.getInstance().init(this);
        Stetho.initializeWithDefaults(this);
        DownloadHelper.getInstance().init(this);
    }
}
