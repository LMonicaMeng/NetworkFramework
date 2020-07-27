package com.btime.networkframework;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.btime.filedownload.file.FileStorageManager;
import com.btime.filedownload.utils.Logger;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        File file = FileStorageManager.getInstance().getFileByNames("http://www.imooc.com");
        Logger.debug("nate","file path = "+file.getAbsoluteFile());
    }
}