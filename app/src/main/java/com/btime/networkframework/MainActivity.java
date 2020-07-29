package com.btime.networkframework;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.btime.filedownload.file.FileStorageManager;
import com.btime.filedownload.http.DownloadCallback;
import com.btime.filedownload.http.DownloadManager;
import com.btime.filedownload.http.HttpManager;
import com.btime.filedownload.utils.Logger;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    private ImageView imageView;
    private ProgressBar mProgressBar;

    private int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.image);
        mProgressBar = findViewById(R.id.progress);

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
        String url = "https://img2.mukewang.com/szimg/5efe95290836b53006000338-360-202.jpg";
        DownloadManager.getInstance().download(url, new DownloadCallback() {
            @Override
            public void success(File file) {
                if(count<1){
                    count ++;
                    return;
                }
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
                Logger.debug("nate", "progress  " + progress);
                mProgressBar.setProgress(progress);
            }
        });
    }

    private void installApk(File file){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.parse("file://"+file.getAbsoluteFile().toString()),"application/vnd.android.package-archive");
        MainActivity.this.startActivity(intent);
    }
}