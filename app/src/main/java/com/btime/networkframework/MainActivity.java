package com.btime.networkframework;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.btime.filedownload.file.FileStorageManager;
import com.btime.filedownload.http.DownloadCallback;
import com.btime.filedownload.http.DownloadManager;
import com.btime.filedownload.http.HttpManager;
import com.btime.filedownload.utils.Logger;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.image);
        File file = FileStorageManager.getInstance().getFileByName("http://www.imooc.com");
        Logger.debug("nate", "file path = " + file.getAbsoluteFile());

//        HttpManager.getInstance().asyncRequest("https://img2.mukewang.com/szimg/5efe95290836b53006000338-360-202.jpg", new DownloadCallback() {
//            @Override
//            public void success(File file) {
//                final Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        imageView.setImageBitmap(bitmap);
//                    }
//                });
//                Logger.debug("nate","success "+file.getAbsoluteFile());
//            }
//
//            @Override
//            public void fail(int errorCode, String errorMessage) {
//                Logger.debug("nate","fail "+errorCode+" "+errorMessage);
//            }
//
//            @Override
//            public void progress(int progress) {
//
//            }
//        });
        DownloadManager.getInstance().download("https://img2.mukewang.com/szimg/5edf24fd081fde7906000338-360-202.jpg", new DownloadCallback() {
            @Override
            public void success(File file) {
                final Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        imageView.setImageBitmap(bitmap);
                    }
                });
                Logger.debug("nate", "success " + file.getAbsoluteFile());
            }

            @Override
            public void fail(int errorCode, String errorMessage) {
                Logger.debug("nate", "fail " + errorCode + " " + errorMessage);
            }

            @Override
            public void progress(int progress) {

            }
        });
    }
}