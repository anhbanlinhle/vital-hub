package com.example.vital_hub.client.fastapi.controller;

import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.example.vital_hub.client.controller.Controller;
import com.example.vital_hub.client.fastapi.objects.PushUpResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class VideoApi {
    static Gson gson = new GsonBuilder()
            .setLenient()
            .create();
    static OkHttpClient client;
    static Retrofit videoRetrofit;
    static VideoController videoController;
    static Call<PushUpResponse> pushupCall;
    public static void initFastapi(String server) {
        String url = "http://" + server + ":7979/";
        client = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();
        videoRetrofit = new Retrofit.Builder()
                .baseUrl(url)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        videoController = videoRetrofit.create(VideoController.class);
    }

    public static void initPushupCall(String videoPath) {
        File videoFile = new File(videoPath);
        RequestBody videoRequestBody = RequestBody.create(MediaType.parse("video/*"), videoFile);
        MultipartBody.Part videoPart = MultipartBody.Part.createFormData("video_data", videoFile.getName(), videoRequestBody);
        pushupCall = videoController.sendVideo(videoPart);
    }
}
