package com.demo.http.download;

import java.io.IOException;

import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RangeHttp {

    public static void main(String[] args) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url("https://img1.gtimg.com/ninja/2/2020/07/ninja159557466990191.jpg")
                .addHeader("Accept-Encoding","identity") //如果服务器支持content-length返回否则返回原编码
                .addHeader("Range","bytes=0-2")
                .build();
        try {
            Response response = client.newCall(request).execute();
            System.out.println("content-length :"+response.body().contentLength());
            if(response.isSuccessful()){
                Headers headers = response.headers();
                for (int i = 0; i < headers.size(); i++) {
                    System.out.println(headers.name(i)+" : "+headers.value(i));
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
