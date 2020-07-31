package com.btime.filedownload.http;

import com.btime.filedownload.DownloadConfig;
import com.btime.filedownload.DownloadTask;
import com.btime.filedownload.db.DownloadEntity;
import com.btime.filedownload.db.DownloadHelper;
import com.btime.filedownload.file.FileStorageManager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class DownloadManager {
    public final static int MAX_THREAD = 2;
    public final static int LOCAL_PROGRESS_SIZE = 1;
    private static final DownloadManager sManager = new DownloadManager();
    public static ExecutorService sLocalProgressPool = Executors.newFixedThreadPool(1);
    private List<DownloadEntity> mCache;
    private HashSet<DownloadTask> mHashSet = new HashSet<>();
    private long mLength;

    private static ThreadPoolExecutor sThreadPool = new ThreadPoolExecutor(MAX_THREAD, MAX_THREAD, 60, TimeUnit.MILLISECONDS, new SynchronousQueue<Runnable>(), new ThreadFactory() {
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

    public void init(DownloadConfig config){
        sThreadPool = new ThreadPoolExecutor(config.getCoreThreadSize(), config.getMaxThreadSize(), 60, TimeUnit.MILLISECONDS, new SynchronousQueue<Runnable>(), new ThreadFactory() {
            private AtomicInteger mInteger = new AtomicInteger();

            @Override
            public Thread newThread(Runnable runnable) {
                Thread thread = new Thread(runnable, "download thread #" + mInteger.getAndIncrement());
                return thread;
            }
        });
        sLocalProgressPool = Executors.newFixedThreadPool(config.getLocalProgressThreadSize());
    }

    private void finish(DownloadTask task) {
        mHashSet.remove(task);
    }

    public void download(final String url, final DownloadCallback callback) {

        final DownloadTask task = new DownloadTask(url, callback);
        if (mHashSet.contains(task)) {
            callback.fail(HttpManager.TASK_RUNNING_ERROR_CODE, "任务已经执行了");
            return;
        }
        mHashSet.add(task);

        mCache = DownloadHelper.getInstance().getAll(url);
        if (mCache == null || mCache.size() == 0) {
            HttpManager.getInstance().asyncRequest(url, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    finish(task);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (!response.isSuccessful() && callback != null) {
                        callback.fail(HttpManager.NETWORK_CODE, "网络出问题了");
                        return;
                    }

                    mLength = response.body().contentLength();
                    if (mLength == -1) {
                        callback.fail(HttpManager.CONTENT_LENGTH_ERROR_CODE, "content length -1");
                        return;
                    }
                    processDownload(url, mLength, callback, mCache);
                    finish(task);
                }
            });

        } else {
            //处理已经下载过的数据
            for (int i = 0; i < mCache.size(); i++) {
                DownloadEntity entity = mCache.get(i);
                if (i == mCache.size() - 1) {
                    mLength = entity.getEnd_position() + 1;
                }
                long startSize = entity.getStart_position() + entity.getProgress_position();
                long endSize = entity.getEnd_position();
                sThreadPool.execute(new DownloadRunnable(startSize, endSize, url, callback, entity));
            }
            sLocalProgressPool.execute(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        try {
                            Thread.sleep(500);
                            File file = FileStorageManager.getInstance().getFileByName(url);
                            long fileSize = file.length();
                            int progress = (int) (fileSize*100.0/mLength);
                            if(progress >= 100){
                                callback.progress(progress);
                                return;
                            }
                            callback.progress(progress);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
    }

    private void processDownload(String url, long length, DownloadCallback callback, List<DownloadEntity> cache) {
        long threadDownloadSize = length / MAX_THREAD;
        if (cache == null || cache.size() == 0) {
            mCache = new ArrayList<>();
        }
        for (int i = 0; i < MAX_THREAD; i++) {
            DownloadEntity entity = new DownloadEntity();
            long startSize = i * threadDownloadSize;
            long endSize = 0;
            if (endSize == MAX_THREAD - 1) {
                endSize = length - 1;
            } else {
                endSize = (i + 1) * threadDownloadSize - 1;
            }
            entity.setDownload_url(url);
            entity.setStart_position(startSize);
            entity.setEnd_position(endSize);
            entity.setThread_id(i + 1);

            sThreadPool.execute(new DownloadRunnable(startSize, endSize, url, callback, entity));
        }
    }
}
