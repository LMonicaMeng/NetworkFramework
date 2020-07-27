package com.btime.filedownload.http;

import android.content.Context;

import com.btime.filedownload.file.FileStorageManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 *
 */
public class HttpManager {

    public static final HttpManager sManager = new HttpManager();
    private static final int NETWORK_CODE = 1;

    private Context mContext;

    private OkHttpClient mClient;

    private HttpManager() {
        mClient = new OkHttpClient();
    }

    public static HttpManager getInstance() {
        return sManager;
    }

    public void init(Context context) {
        this.mContext = context;
    }

    /**
     * 同步请求
     *
     * @param url
     * @return
     */
    public Response syncRequest(String url) {
        Request request = new Request.Builder().url(url).build();
        try {
            return mClient.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 异步请求
     *
     * @param url
     * @param callback
     */
    public void asyncRequest(final String url, final DownloadCallback callback) {
        Request request = new Request.Builder().url(url).build();
        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful() && callback != null) {
                    callback.fail(NETWORK_CODE, "请求失败");
                }

                File file = FileStorageManager.getInstance().getFileByName(url);
                byte[] buffer = new byte[1024*500];
                int len;
                FileOutputStream fileOut = new FileOutputStream(file);
                InputStream inStream = response.body().byteStream();
                while ((len = inStream.read(buffer,0,buffer.length))!= -1){
                    fileOut.write(buffer,0,len);
                    fileOut.flush();
                }
                assert callback != null;
                callback.success(file);
            }
        });
    }

}
