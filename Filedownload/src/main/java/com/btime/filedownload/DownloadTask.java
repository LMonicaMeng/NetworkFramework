package com.btime.filedownload;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.btime.filedownload.http.DownloadCallback;

import java.util.Objects;

public class DownloadTask {
    private String mUrl;

    private DownloadCallback mCallback;

    public DownloadTask(String mUrl, DownloadCallback mCallback) {
        this.mUrl = mUrl;
        this.mCallback = mCallback;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String mUrl) {
        this.mUrl = mUrl;
    }

    public DownloadCallback getmCallback() {
        return mCallback;
    }

    public void setmCallback(DownloadCallback mCallback) {
        this.mCallback = mCallback;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DownloadTask)) return false;
        DownloadTask that = (DownloadTask) o;
        return mUrl.equals(that.mUrl) &&
                getmCallback().equals(that.getmCallback());
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public int hashCode() {
        return Objects.hash(mUrl, getmCallback());
    }
}
