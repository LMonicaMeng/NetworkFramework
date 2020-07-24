package com.demo.http.lib;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MultipartHttp {

    public static void main(String[] args) {

        RequestBody imageBody = RequestBody.create(MediaType.parse("image/jpeg"),new File("/Users/nate/girl.jpg"));
        OkHttpClient client = new OkHttpClient();
        MultipartBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("name","nate")  //需要指定setType为Form类型
                .addFormDataPart("filename","girl.jpg",imageBody)
                .build();
        Request request = new Request.Builder().url("http://192.168.1.6:8080/web/UploadServlet").post(body).build();
        try {
            Response response = client.newCall(request).execute();
            if(response.isSuccessful()){
                System.out.println(response.body().string());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
