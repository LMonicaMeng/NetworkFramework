package com.demo.http.lib;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PostHttp {

    public static void main(String[] args) {

        OkHttpClient client = new OkHttpClient();
        FormBody body = new FormBody.Builder().add("username","name")
                .add("userage","99").build();
        Request request = new Request.Builder().url("http://localhost:8080/web/HelloServlet").post(body).build();
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
