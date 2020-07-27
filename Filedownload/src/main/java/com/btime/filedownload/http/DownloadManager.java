package com.btime.filedownload.http;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class DownloadManager {
    private final static int MAX_THREAD = 2;
    private static final DownloadManager sManager = new DownloadManager();

    private static final ThreadPoolExecutor sThreadPool = new ThreadPoolExecutor(MAX_THREAD, MAX_THREAD, 60, TimeUnit.MILLISECONDS, new SynchronousQueue<Runnable>(), new ThreadFactory() {
        private AtomicInteger mInteger = new AtomicInteger();

        @Override
        public Thread newThread(Runnable runnable) {
            Thread thread = new Thread(runnable, "download thread #" + mInteger.getAndIncrement());
            return thread;
        }
    });

    public static DownloadManager getInstance() {
        return sManager;
    }

    private DownloadManager() {

    }

    public void download(final String url, final DownloadCallback callback) {
        HttpManager.getInstance().asyncRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful() && callback != null) {
                    callback.fail(HttpManager.NETWORK_CODE, "网络出问题了");
                    return;
                }

                long length = response.body().contentLength();
                if (length == -1) {
                    callback.fail(HttpManager.CONTENT_LENGTH_ERROR_CODE, "content length -1");
                    return;
                }

                processDownload(url,length,callback);
            }
        });
    }

    private static void processDownload(String url, long length, DownloadCallback callback) {
        long threadDownloadSize = length / MAX_THREAD;
        for (int i = 0; i < MAX_THREAD; i++) {
            long startSize = i * threadDownloadSize;
            long endSize = (i + 1) * threadDownloadSize - 1;
            sThreadPool.execute(new DownloadRunnable(startSize, endSize, url, callback));
        }
    }
}
